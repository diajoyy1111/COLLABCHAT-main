document.addEventListener('DOMContentLoaded', function(){
    loadGroups();
    document.getElementById('createGroupForm')?.addEventListener('submit', function(e){
        e.preventDefault();
        const name = document.getElementById('groupName').value;
        fetch('group', { method: 'POST', headers: {'Content-Type':'application/x-www-form-urlencoded'}, body: 'name='+encodeURIComponent(name) })
            .then(r=>r.json()).then(j=>window.location.href='chat.jsp?groupId='+j.groupId).catch(()=>alert('Could not create group'));
    });

    document.getElementById('joinGroupForm')?.addEventListener('submit', function(e){
        e.preventDefault();
        const groupId = document.getElementById('joinGroupId').value;
        // Use POST with explicit action=join so the servlet can read parameters reliably
        fetch('group', { method: 'POST', headers: {'Content-Type':'application/x-www-form-urlencoded'}, body: 'groupId='+encodeURIComponent(groupId)+'&action=join' })
            .then(r=>{ if (r.ok) window.location.href='chat.jsp?groupId='+groupId; else alert('Could not join'); })
            .catch(()=>alert('Could not join'));
    });

    document.getElementById('openWhiteboard')?.addEventListener('click', function(){ document.getElementById('whiteboardModal').style.display='flex'; });
    document.getElementById('openMiniGame')?.addEventListener('click', function(){ document.getElementById('miniGameModal').style.display='flex'; });
});

function loadGroups(){
    fetch('group').then(r=>r.json()).then(list=>{
        const el = document.getElementById('groups');
        el.innerHTML='';
        list.forEach(g=>{
            const d = document.createElement('div');
            d.className='group-item';
            d.innerHTML = `<strong>${g.name}</strong> <small>#${g.groupId}</small> <a href="chat.jsp?groupId=${g.groupId}">Open</a>`;
            el.appendChild(d);
        });
    }).catch(()=>{});
}

function closeWhiteboard(){ document.getElementById('whiteboardModal').style.display='none'; }
function closeMiniGame(){ document.getElementById('miniGameModal').style.display='none'; }
