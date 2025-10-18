<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) session.getAttribute("username");
    Integer userId = (Integer) session.getAttribute("userId");
    Integer groupId = null;
    try { groupId = Integer.parseInt(request.getParameter("groupId")); } catch (Exception ignored) {}
    if (username == null || userId == null || groupId == null) {
        response.sendRedirect("home.jsp");
        return;
    }
    String encUser = java.net.URLEncoder.encode(username, "UTF-8");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat - CollabChat</title>
    <link rel="stylesheet" href="styles.css" />
    <script>
        // Safely emit server-side values into JS using URL-encoding to avoid injection
        const CURRENT_USER = { id: <%= userId %>, name: decodeURIComponent("<%= encUser %>") };
        const CURRENT_GROUP = <%= groupId %>;
    </script>
</head>
<body>
<div class="topbar">
    <div class="brand">CollabChat</div>
        <div class="user">
            <span>Group: <strong id="groupName">#<%= groupId %></strong></span>
            <a href="home.jsp">← Back to Home</a>
        </div>
</div>

    <div class="container" style="height: calc(100vh - 80px); display: flex; flex-direction: column;">
        <!-- Chat Header -->
        <div style="text-align: center; margin-bottom: 20px; padding: 20px; background: linear-gradient(135deg, #f8f9fa, #e9ecef); border-radius: 15px;">
            <h1 style="color: #2c3e50; font-size: 28px; margin-bottom: 10px;">💬 Group Chat</h1>
            <p style="color: #6c757d; font-size: 16px;">
                Group: <strong id="groupNameDisplay">Loading...</strong>
                <button onclick="loadGroupName()" style="margin-left: 10px; padding: 4px 8px; font-size: 12px; background: #3498db; color: white; border: none; border-radius: 4px; cursor: pointer;">🔄 Refresh</button>
            </p>
        </div>
        
        <!-- Chat Container -->
        <div class="chat-container" style="flex: 1;">
            <div id="messages" class="messages">
                <div style="text-align: center; padding: 40px; color: #6c757d;">
                    <div class="loading"></div>
                    <p style="margin-top: 15px;">Loading messages...</p>
                </div>
            </div>
            
            <!-- Typing Indicator -->
            <div id="typingIndicator" style="padding: 10px 20px; background: #e9ecef; color: #6c757d; font-style: italic; display: none;">
                Someone is typing...
            </div>
            
    <form id="msgForm" class="msg-form">
                <input type="text" id="msgInput" placeholder="Type your message..." autocomplete="off" required />
        <button type="submit">Send</button>
    </form>
</div>

        <!-- Chat Features -->
        <div style="margin-top: 20px; display: flex; gap: 15px; flex-wrap: wrap;">
            <button onclick="clearChat()" style="background: linear-gradient(135deg, #e74c3c, #c0392b); font-size: 12px; padding: 8px 15px;">
                🗑️ Clear Chat
            </button>
            <button onclick="exportChat()" style="background: linear-gradient(135deg, #27ae60, #2ecc71); font-size: 12px; padding: 8px 15px;">
                📥 Export Messages
            </button>
            <button onclick="toggleNotifications()" id="notifyBtn" style="background: linear-gradient(135deg, #3498db, #2980b9); font-size: 12px; padding: 8px 15px;">
                🔔 Enable Notifications
            </button>
        </div>
    </div>

    <!-- Removed chat.js to prevent conflicts with enhanced functionality -->
    
    <script>
        // Enhanced chat functionality
        let notificationsEnabled = false;
        let typingTimer = null;
        let isTyping = false;
        
        // Enhanced message rendering
        function renderMessage(msg) {
            const messagesEl = document.getElementById('messages');
            const messageDiv = document.createElement('div');
            
            const isOwn = msg.userId === CURRENT_USER.id;
            messageDiv.className = 'message ' + (isOwn ? 'own' : 'other');
            
            // Create proper timestamp from message data
            let timeString;
            if (msg.timestamp) {
                const msgDate = new Date(msg.timestamp);
                timeString = msgDate.toLocaleString(); // Full date and time
            } else if (msg.ts) {
                // Handle legacy timestamp format
                timeString = msg.ts;
            } else {
                timeString = new Date().toLocaleString();
            }
            
            messageDiv.innerHTML = 
                '<div class="message-header">' + (msg.username || 'Anonymous') + ' • ' + timeString + '</div>' +
                '<div>' + escapeHtml(msg.text || '') + '</div>';
            
            messagesEl.appendChild(messageDiv);
            messagesEl.scrollTop = messagesEl.scrollHeight;
            
            // Show notification if enabled and not own message
            if (notificationsEnabled && !isOwn) {
                showNotification((msg.username || 'Anonymous') + ': ' + (msg.text || ''));
            }
        }
        
        // HTML escaping for security
        function escapeHtml(text) {
            const div = document.createElement('div');
            div.textContent = text;
            return div.innerHTML;
        }
        
        // Enhanced message loading
        function loadMessages() {
            fetch('message?groupId=' + CURRENT_GROUP)
                .then(r => r.json())
                .then(list => {
                    const messagesEl = document.getElementById('messages');
                    messagesEl.innerHTML = '';
                    
                    if (list.length === 0) {
                        messagesEl.innerHTML = 
                            '<div style="text-align: center; padding: 40px; color: #6c757d;">' +
                                '<h3>No messages yet</h3>' +
                                '<p>Start the conversation by sending a message!</p>' +
                            '</div>';
                        return;
                    }
                    
                    list.forEach(renderMessage);
                })
                .catch(() => {
                    const messagesEl = document.getElementById('messages');
                    messagesEl.innerHTML = 
                        '<div style="text-align: center; padding: 40px; color: #e74c3c;">' +
                            '<h3>Error loading messages</h3>' +
                            '<p>Please refresh the page</p>' +
                        '</div>';
                });
        }
        
        // Enhanced message sending
        function sendMessage(text) {
            fetch('message', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'groupId=' + encodeURIComponent(CURRENT_GROUP) + '&message=' + encodeURIComponent(text)
            })
            .then(r => {
                if (r.status === 201) {
                    loadMessages(); // Reload to show the new message
                } else {
                    alert('Failed to send message');
                }
            })
            .catch(() => alert('Failed to send message'));
        }
        
        // Typing indicator
        function showTypingIndicator() {
            const indicator = document.getElementById('typingIndicator');
            indicator.style.display = 'block';
            
            clearTimeout(typingTimer);
            typingTimer = setTimeout(() => {
                indicator.style.display = 'none';
            }, 2000);
        }
        
        // Notification functions
        function showNotification(message) {
            if ('Notification' in window && Notification.permission === 'granted') {
                new Notification('CollabChat', {
                    body: message,
                    icon: 'data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><text y=".9em" font-size="90">🎓</text></svg>'
                });
            }
        }
        
        function toggleNotifications() {
            const btn = document.getElementById('notifyBtn');
            
            if (!notificationsEnabled) {
                if ('Notification' in window) {
                    Notification.requestPermission().then(permission => {
                        if (permission === 'granted') {
                            notificationsEnabled = true;
                            btn.textContent = '🔔 Notifications Enabled';
                            btn.style.background = 'linear-gradient(135deg, #27ae60, #2ecc71)';
                        } else {
                            alert('Notifications blocked. Please enable them in your browser settings.');
                        }
                    });
                } else {
                    alert('This browser does not support notifications.');
                }
            } else {
                notificationsEnabled = false;
                btn.textContent = '🔔 Enable Notifications';
                btn.style.background = 'linear-gradient(135deg, #3498db, #2980b9)';
            }
        }
        
        // Chat management functions
        function clearChat() {
            if (confirm('Are you sure you want to clear all messages? This action cannot be undone.')) {
                // This would need a server endpoint to clear messages
                alert('Clear chat feature requires server implementation');
            }
        }
        
        function exportChat() {
            const messages = document.querySelectorAll('.message');
            let exportText = 'CollabChat Export - Group #' + CURRENT_GROUP + '\n';
            exportText += 'Exported on: ' + new Date().toLocaleString() + '\n\n';
            
            messages.forEach(msg => {
                const header = msg.querySelector('.message-header').textContent;
                const text = msg.querySelector('div:last-child').textContent;
                exportText += header + '\n' + text + '\n\n';
            });
            
            const blob = new Blob([exportText], { type: 'text/plain' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'collabchat-group-' + CURRENT_GROUP + '-' + new Date().toISOString().split('T')[0] + '.txt';
            a.click();
            URL.revokeObjectURL(url);
        }
        
        // Load group name
        function loadGroupName() {
            console.log('Loading group name for group ID:', CURRENT_GROUP);
            
            // Try to get specific group first
            fetch('group?groupId=' + CURRENT_GROUP)
                .then(r => {
                    console.log('Group response status:', r.status);
                    if (!r.ok) {
                        throw new Error('HTTP ' + r.status);
                    }
                    return r.json();
                })
                .then(group => {
                    console.log('Group data received:', group);
                    const groupNameEl = document.getElementById('groupNameDisplay');
                    if (group && group.name) {
                        groupNameEl.textContent = group.name;
                        console.log('Group name set to:', group.name);
                    } else {
                        throw new Error('No group name found');
                    }
                })
                .catch(error => {
                    console.error('Error loading specific group, trying fallback:', error);
                    // Fallback: try to get from user's groups list
                    fetch('group')
                        .then(r => r.json())
                        .then(groups => {
                            console.log('User groups received:', groups);
                            const currentGroup = groups.find(g => g.groupId == CURRENT_GROUP);
                            const groupNameEl = document.getElementById('groupNameDisplay');
                            if (currentGroup && currentGroup.name) {
                                groupNameEl.textContent = currentGroup.name;
                                console.log('Group name set from user groups:', currentGroup.name);
                            } else {
                                groupNameEl.textContent = 'Group #' + CURRENT_GROUP;
                                console.log('Using fallback group name');
                            }
                        })
                        .catch(fallbackError => {
                            console.error('Fallback also failed:', fallbackError);
                            document.getElementById('groupNameDisplay').textContent = 'Group #' + CURRENT_GROUP;
                        });
                });
        }
        
        // Enhanced form handling
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('msgForm');
            const input = document.getElementById('msgInput');
            
            // Load group name first
            loadGroupName();
            
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                const text = input.value.trim();
                if (!text) return;
                
                sendMessage(text);
                input.value = '';
            });
            
            // Typing indicator
            input.addEventListener('input', function() {
                if (!isTyping) {
                    isTyping = true;
                    showTypingIndicator();
                }
                
                clearTimeout(typingTimer);
                typingTimer = setTimeout(() => {
                    isTyping = false;
                }, 1000);
            });
            
            // Auto-focus input
            input.focus();
            
            // Load initial messages
            loadMessages();
            
            // Polling for new messages (reduced frequency to prevent duplication)
            setInterval(loadMessages, 5000);
        });
        
        // Keyboard shortcuts
        document.addEventListener('keydown', function(e) {
            if (e.ctrlKey && e.key === 'Enter') {
                const input = document.getElementById('msgInput');
                const text = input.value.trim();
                if (text) {
                    sendMessage(text);
                    input.value = '';
                }
            }
        });
    </script>
</body>
</html>
