document.addEventListener('DOMContentLoaded', function(){
    const messagesEl = document.getElementById('messages');
    const form = document.getElementById('msgForm');
    const input = document.getElementById('msgInput');

    form.addEventListener('submit', function(e){
        e.preventDefault();
        const text = input.value.trim();
        if (!text) return;
        fetch('message', { method:'POST', headers: {'Content-Type':'application/x-www-form-urlencoded'}, body: 'groupId='+encodeURIComponent(CURRENT_GROUP)+'&message='+encodeURIComponent(text) })
            .then(r=>{ if (r.status===201) { input.value=''; load(); } else alert('Failed to send'); })
            .catch(()=>alert('Failed to send'));
    });

    function render(msg){
        const d = document.createElement('div');
        d.className='message';
        const ts = msg.ts || '';
        const user = msg.username || 'anon';
        const text = msg.text || '';
        d.textContent = `[${ts}] ${user}: ${text}`;
        messagesEl.appendChild(d);
    }

    function load(){
        fetch('message?groupId='+CURRENT_GROUP).then(r=>r.json()).then(list=>{
            messagesEl.innerHTML='';
            list.forEach(render);
            messagesEl.scrollTop = messagesEl.scrollHeight;
        }).catch(()=>{});
    }

    // Polling for now; replace with WebSocket later
    load();
    setInterval(load, 3000);

    // Placeholder for future WebSocket integration
    // function initWebSocket(){
    //   const ws = new WebSocket(`ws://${location.host}/collabchat/ws?groupId=${CURRENT_GROUP}`);
    //   ws.onmessage = (ev)=>{ try { const msg = JSON.parse(ev.data); render(msg); } catch(e){} };
    //   form.addEventListener('submit', (e)=>{ e.preventDefault(); const text=input.value.trim(); if(!text) return; ws.send(JSON.stringify({groupId:CURRENT_GROUP,text})); input.value=''; });
    // }
});
