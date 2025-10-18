<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%
    String username = (String) session.getAttribute("username");
    Integer userId = (Integer) session.getAttribute("userId");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - CollabChat</title>
    <link rel="stylesheet" href="styles.css" />
    <script src="app.js"></script>
</head>
<body>
    <div class="topbar">
        <div class="brand">CollabChat</div>
        <div class="user">
            <span>Welcome, <strong><%= username %></strong></span>
            <a href="logout">Logout</a>
        </div>
    </div>
    <!-- Modern UI chooser modal (client-side) -->
    <div id="modernUiModal" style="position:fixed;left:0;top:0;right:0;bottom:0;background:rgba(0,0,0,0.45);display:flex;align-items:center;justify-content:center;z-index:9999;">
        <div style="background:white;padding:20px;border-radius:12px;max-width:420px;width:90%;box-shadow:0 10px 30px rgba(0,0,0,0.2);text-align:center;">
            <h2 style="margin-bottom:8px;color:#2c3e50;">Try the Modern UI</h2>
            <p style="color:#6c757d;margin-bottom:18px;">We packaged a modern single-page HTML UI. Would you like to open it in a new tab?</p>
            <div style="display:flex;gap:10px;justify-content:center;">
                <button id="modernYes" style="background: linear-gradient(135deg,#2980b9,#3498db);color:#fff;border:none;padding:10px 16px;border-radius:8px;cursor:pointer;font-weight:600;">Yes — Open</button>
                <button id="modernNo" style="background:#f1f3f5;border:none;padding:10px 16px;border-radius:8px;cursor:pointer;">No — Stay</button>
            </div>
            <div style="margin-top:12px;font-size:12px;color:#95a5a6;">You can always open it later from Quick Actions.</div>
        </div>
    </div>
    
    <div class="container">
        <!-- Welcome Section -->
        <div style="text-align: center; margin-bottom: 40px;">
            <h1 style="color: #2c3e50; font-size: 36px; margin-bottom: 15px;">🎓 Welcome to CollabChat!</h1>
            <p style="color: #6c757d; font-size: 18px;">Your collaborative classroom platform is ready. Join groups, chat, and collaborate in real-time.</p>
        </div>
        
        <!-- Feature Overview -->
        <div class="feature-grid">
            <div class="feature-card">
                <h3>💬 Group Chat</h3>
                <p>Join existing groups or create new ones to start collaborating with your classmates and teachers.</p>
            </div>
            <div class="feature-card">
                <h3>🎨 Shared Whiteboard</h3>
                <p>Draw, sketch, and brainstorm together on a collaborative canvas that updates in real-time.</p>
            </div>
            <div class="feature-card">
                <h3>🎮 Educational Games</h3>
                <p>Play interactive games like Tic-Tac-Toe and memory matching with your peers.</p>
            </div>
            <div class="feature-card">
                <h3>📝 Live Quizzes</h3>
                <p>Teachers can create quizzes and students can answer them in real-time with instant feedback.</p>
            </div>
        </div>
        
        <!-- Groups Section -->
        <div style="margin: 40px 0;">
            <h2 style="color: #2c3e50; margin-bottom: 20px;">Your Groups</h2>
            <div id="groups" style="min-height: 100px;">
                <div style="text-align: center; padding: 40px; color: #6c757d;">
                    <div class="loading"></div>
                    <p style="margin-top: 15px;">Loading your groups...</p>
                </div>
            </div>
        </div>
        
        <!-- Group Management -->
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 30px; margin: 40px 0;">
            <!-- Create Group -->
            <div style="background: #f8f9fa; padding: 25px; border-radius: 15px; border-left: 4px solid #27ae60;">
                <h3 style="color: #2c3e50; margin-bottom: 20px;">➕ Create New Group</h3>
                <form id="createGroupForm">
                    <div class="form-group">
                        <label for="groupName">Group Name</label>
                        <input type="text" id="groupName" placeholder="Enter group name" required />
                    </div>
                    <button type="submit" style="width: 100%; background: linear-gradient(135deg, #27ae60, #2ecc71);">
                        Create Group
                    </button>
                </form>
            </div>
            
            <!-- Join Group -->
            <div style="background: #f8f9fa; padding: 25px; border-radius: 15px; border-left: 4px solid #3498db;">
                <h3 style="color: #2c3e50; margin-bottom: 20px;">🔗 Join Existing Group</h3>
                <form id="joinGroupForm">
                    <div class="form-group">
                        <label for="joinGroupId">Group ID</label>
                        <input type="number" id="joinGroupId" placeholder="Enter group ID" required />
                    </div>
                    <button type="submit" style="width: 100%;">
                        Join Group
                    </button>
                </form>
            </div>
        </div>
        
        <!-- Quick Actions -->
        <div style="margin: 40px 0;">
            <h2 style="color: #2c3e50; margin-bottom: 20px;">Quick Actions</h2>
            <div style="display: flex; gap: 15px; flex-wrap: wrap; align-items: center;">
                <button id="openWhiteboard" style="background: linear-gradient(135deg, #e67e22, #f39c12);">
                    🎨 Open Whiteboard
                </button>
                <button id="openMiniGame" style="background: linear-gradient(135deg, #9b59b6, #8e44ad);">
                    🎮 Play Games
                </button>
                <button id="openQuiz" style="background: linear-gradient(135deg, #e74c3c, #c0392b);">
                    📝 Take Quiz
                </button>
                <!-- Link to the standalone FX-style HTML UI packaged with the app -->
                <a href="collabchat-fx.html" target="_blank" style="text-decoration:none;">
                    <button id="openModernUI" style="background: linear-gradient(135deg, #2980b9, #3498db); color: white; padding: 12px 18px; border-radius: 8px; border: none; cursor: pointer; font-weight: 600;">
                        🚀 Open Modern UI
                    </button>
                </a>
            </div>
        </div>
    </div>
    
    <!-- Whiteboard Modal -->
    <div id="whiteboardModal" class="modal" style="display:none;">
        <div class="modal-content" style="max-width: 95%; max-height: 95%;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <h3 style="color: #2c3e50;">🎨 Collaborative Whiteboard</h3>
                <button onclick="closeWhiteboard()" style="background: #e74c3c; padding: 8px 15px; font-size: 12px;">Close</button>
            </div>
            
            <div class="whiteboard-container">
                <div class="whiteboard-toolbar">
                    <div class="tool-group">
                        <label>Colors:</label>
                        <div class="color-btn active" style="background: #000;" onclick="selectColor('#000')"></div>
                        <div class="color-btn" style="background: #e74c3c;" onclick="selectColor('#e74c3c')"></div>
                        <div class="color-btn" style="background: #3498db;" onclick="selectColor('#3498db')"></div>
                        <div class="color-btn" style="background: #27ae60;" onclick="selectColor('#27ae60')"></div>
                        <div class="color-btn" style="background: #f39c12;" onclick="selectColor('#f39c12')"></div>
                        <div class="color-btn" style="background: #9b59b6;" onclick="selectColor('#9b59b6')"></div>
                    </div>
                    <div class="tool-group">
                        <label>Brush Size:</label>
                        <select id="brushSize" onchange="changeBrushSize()">
                            <option value="2">Thin</option>
                            <option value="4" selected>Medium</option>
                            <option value="8">Thick</option>
                            <option value="12">Extra Thick</option>
                        </select>
                    </div>
                    <div class="tool-group">
                        <button onclick="clearWhiteboard()" style="background: #e74c3c;">Clear</button>
                        <button onclick="undoLastStroke()" style="background: #6c757d;">Undo</button>
                    </div>
                </div>
                <canvas id="whiteboardCanvas" class="whiteboard-canvas"></canvas>
            </div>
        </div>
    </div>
    
    <!-- Games Modal -->
    <div id="miniGameModal" class="modal" style="display:none;">
        <div class="modal-content">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <h3 style="color: #2c3e50;">🎮 Educational Games</h3>
                <button onclick="closeMiniGame()" style="background: #e74c3c; padding: 8px 15px; font-size: 12px;">Close</button>
            </div>
            
            <div class="game-container">
                <div class="game-selection">
                    <div class="game-card" onclick="startTicTacToe()">
                        <h3>🎯 Tic-Tac-Toe</h3>
                        <p>Classic 3x3 grid game</p>
                    </div>
                    <div class="game-card" onclick="startMemoryGame()">
                        <h3>🧠 Memory Match</h3>
                        <p>Find matching pairs</p>
                    </div>
                </div>
                
                <div id="ticTacToeGame" class="hidden">
                    <h3 style="margin-bottom: 20px;">Tic-Tac-Toe</h3>
                    <div class="tic-tac-toe" id="tttGrid">
                        <div class="ttt-cell" onclick="makeMove(0)"></div>
                        <div class="ttt-cell" onclick="makeMove(1)"></div>
                        <div class="ttt-cell" onclick="makeMove(2)"></div>
                        <div class="ttt-cell" onclick="makeMove(3)"></div>
                        <div class="ttt-cell" onclick="makeMove(4)"></div>
                        <div class="ttt-cell" onclick="makeMove(5)"></div>
                        <div class="ttt-cell" onclick="makeMove(6)"></div>
                        <div class="ttt-cell" onclick="makeMove(7)"></div>
                        <div class="ttt-cell" onclick="makeMove(8)"></div>
                    </div>
                    <button onclick="resetTicTacToe()" style="background: linear-gradient(135deg, #27ae60, #2ecc71); margin-top: 20px;">Reset Game</button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Quiz Modal -->
    <div id="quizModal" class="modal" style="display:none;">
        <div class="modal-content">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <h3 style="color: #2c3e50;">📝 Live Quiz</h3>
                <button onclick="closeQuiz()" style="background: #e74c3c; padding: 8px 15px; font-size: 12px;">Close</button>
            </div>
            
            <div class="quiz-container">
                <div id="teacherQuizControls" class="quiz-controls">
                    <input type="text" id="quizQuestion" placeholder="Enter your question...">
                    <input type="text" id="option1" placeholder="Option 1">
                    <input type="text" id="option2" placeholder="Option 2">
                    <input type="text" id="option3" placeholder="Option 3">
                    <input type="text" id="option4" placeholder="Option 4">
                    <button onclick="sendQuiz()">Send Quiz</button>
                </div>
                
                <div id="studentQuiz" class="hidden">
                    <div class="quiz-timer" id="quizTimer">Time: 30s</div>
                    <div class="quiz-question" id="quizQuestionDisplay">
                        <h3 id="questionText">Question will appear here...</h3>
                    </div>
                    <div class="quiz-options" id="quizOptions">
                        <!-- Options will be populated here -->
                    </div>
                </div>
                
                <div id="quizWaiting" style="text-align: center; padding: 40px;">
                    <h3 style="color: #6c757d;">Waiting for quiz...</h3>
                    <p style="color: #6c757d;">Your teacher will send a quiz question soon.</p>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        // Enhanced whiteboard functionality
        let currentColor = '#000';
        let currentBrushSize = 4;
        let whiteboardCanvas;
        let whiteboardCtx;
        let isDrawing = false;
        let lastX = 0;
        let lastY = 0;
        let strokes = [];
        
        // Enhanced game functionality
        let tttBoard = ['', '', '', '', '', '', '', '', ''];
        let currentPlayer = 'X';
        
        // Enhanced quiz functionality
        let quizTimer = null;
        let quizTimeLeft = 30;
        
        // Initialize whiteboard when modal opens
        function initWhiteboard() {
            whiteboardCanvas = document.getElementById('whiteboardCanvas');
            whiteboardCtx = whiteboardCanvas.getContext('2d');
            
            // Set canvas size
            const container = whiteboardCanvas.parentElement;
            whiteboardCanvas.width = container.clientWidth - 4;
            whiteboardCanvas.height = container.clientHeight - 4;
            
            // Initialize events
            whiteboardCanvas.addEventListener('mousedown', startDrawing);
            whiteboardCanvas.addEventListener('mousemove', draw);
            whiteboardCanvas.addEventListener('mouseup', stopDrawing);
            whiteboardCanvas.addEventListener('mouseout', stopDrawing);
        }
        
        function startDrawing(e) {
            isDrawing = true;
            const rect = whiteboardCanvas.getBoundingClientRect();
            lastX = e.clientX - rect.left;
            lastY = e.clientY - rect.top;
        }
        
        function draw(e) {
            if (!isDrawing) return;
            
            const rect = whiteboardCanvas.getBoundingClientRect();
            const currentX = e.clientX - rect.left;
            const currentY = e.clientY - rect.top;
            
            whiteboardCtx.beginPath();
            whiteboardCtx.moveTo(lastX, lastY);
            whiteboardCtx.lineTo(currentX, currentY);
            whiteboardCtx.strokeStyle = currentColor;
            whiteboardCtx.lineWidth = currentBrushSize;
            whiteboardCtx.lineCap = 'round';
            whiteboardCtx.stroke();
            
            strokes.push({
                startX: lastX,
                startY: lastY,
                endX: currentX,
                endY: currentY,
                color: currentColor,
                size: currentBrushSize
            });
            
            lastX = currentX;
            lastY = currentY;
        }
        
        function stopDrawing() {
            isDrawing = false;
        }
        
        function selectColor(color) {
            currentColor = color;
            document.querySelectorAll('.color-btn').forEach(btn => {
                btn.classList.remove('active');
            });
            event.target.classList.add('active');
        }
        
        function changeBrushSize() {
            currentBrushSize = parseInt(document.getElementById('brushSize').value);
        }
        
        function clearWhiteboard() {
            whiteboardCtx.clearRect(0, 0, whiteboardCanvas.width, whiteboardCanvas.height);
            strokes = [];
        }
        
        function undoLastStroke() {
            if (strokes.length === 0) return;
            strokes.pop();
            redrawWhiteboard();
        }
        
        function redrawWhiteboard() {
            whiteboardCtx.clearRect(0, 0, whiteboardCanvas.width, whiteboardCanvas.height);
            strokes.forEach(stroke => {
                whiteboardCtx.beginPath();
                whiteboardCtx.moveTo(stroke.startX, stroke.startY);
                whiteboardCtx.lineTo(stroke.endX, stroke.endY);
                whiteboardCtx.strokeStyle = stroke.color;
                whiteboardCtx.lineWidth = stroke.size;
                whiteboardCtx.lineCap = 'round';
                whiteboardCtx.stroke();
            });
        }
        
        // Game functions
        function startTicTacToe() {
            document.querySelector('.game-selection').classList.add('hidden');
            document.getElementById('ticTacToeGame').classList.remove('hidden');
            resetTicTacToe();
        }
        
        function startMemoryGame() {
            alert('Memory Game coming soon!');
        }
        
        function makeMove(index) {
            if (tttBoard[index] !== '') return;
            
            tttBoard[index] = currentPlayer;
            updateTTTDisplay();
            
            if (checkWinner()) {
                setTimeout(() => {
                    alert(`${currentPlayer} wins!`);
                    resetTicTacToe();
                }, 100);
                return;
            }
            
            if (tttBoard.every(cell => cell !== '')) {
                setTimeout(() => {
                    alert('It\'s a tie!');
                    resetTicTacToe();
                }, 100);
                return;
            }
            
            currentPlayer = currentPlayer === 'X' ? 'O' : 'X';
        }
        
        function updateTTTDisplay() {
            const cells = document.querySelectorAll('.ttt-cell');
            cells.forEach((cell, index) => {
                cell.textContent = tttBoard[index];
                cell.className = `ttt-cell ${tttBoard[index].toLowerCase()}`;
            });
        }
        
        function checkWinner() {
            const winningCombinations = [
                [0, 1, 2], [3, 4, 5], [6, 7, 8], // Rows
                [0, 3, 6], [1, 4, 7], [2, 5, 8], // Columns
                [0, 4, 8], [2, 4, 6] // Diagonals
            ];
            
            return winningCombinations.some(combination => {
                const [a, b, c] = combination;
                return tttBoard[a] && tttBoard[a] === tttBoard[b] && tttBoard[a] === tttBoard[c];
            });
        }
        
        function resetTicTacToe() {
            tttBoard = ['', '', '', '', '', '', '', '', ''];
            currentPlayer = 'X';
            updateTTTDisplay();
        }

        // Modern UI modal handlers
        document.addEventListener('DOMContentLoaded', function() {
            const modal = document.getElementById('modernUiModal');
            const yes = document.getElementById('modernYes');
            const no = document.getElementById('modernNo');
            if (!modal) return;
            yes.addEventListener('click', function() {
                window.open('collabchat-fx.html', '_blank');
                modal.style.display = 'none';
            });
            no.addEventListener('click', function() {
                modal.style.display = 'none';
            });
        });
        
        // Quiz functions
        function sendQuiz() {
            const question = document.getElementById('quizQuestion').value.trim();
            const options = [
                document.getElementById('option1').value.trim(),
                document.getElementById('option2').value.trim(),
                document.getElementById('option3').value.trim(),
                document.getElementById('option4').value.trim()
            ];
            
            if (!question || options.some(opt => !opt)) {
                alert('Please fill in all fields');
                return;
            }
            
            // Simulate sending quiz
            console.log('Quiz sent:', { question, options });
            
            // Clear form
            document.getElementById('quizQuestion').value = '';
            options.forEach((_, index) => {
                document.getElementById(`option${index + 1}`).value = '';
            });
            
            alert('Quiz sent to all students!');
        }
        
        function startQuizTimer() {
            quizTimeLeft = 30;
            const timerElement = document.getElementById('quizTimer');
            
            quizTimer = setInterval(() => {
                quizTimeLeft--;
                timerElement.textContent = `Time: ${quizTimeLeft}s`;
                
                if (quizTimeLeft <= 0) {
                    clearInterval(quizTimer);
                    timerElement.textContent = 'Time\'s up!';
                    timerElement.style.background = '#6c757d';
                }
            }, 1000);
        }
        
        // Modal functions
        function closeWhiteboard() {
            document.getElementById('whiteboardModal').style.display = 'none';
        }
        
        function closeMiniGame() {
            document.getElementById('miniGameModal').style.display = 'none';
        }
        
        function closeQuiz() {
            document.getElementById('quizModal').style.display = 'none';
        }
        
        // Enhanced event listeners
        document.addEventListener('DOMContentLoaded', function() {
            // Whiteboard modal
            document.getElementById('openWhiteboard')?.addEventListener('click', function() {
                document.getElementById('whiteboardModal').style.display = 'flex';
                setTimeout(initWhiteboard, 100);
            });
            
            // Games modal
            document.getElementById('openMiniGame')?.addEventListener('click', function() {
                document.getElementById('miniGameModal').style.display = 'flex';
            });
            
            // Quiz modal
            document.getElementById('openQuiz')?.addEventListener('click', function() {
                document.getElementById('quizModal').style.display = 'flex';
            });
        });
    </script>
</body>
</html>
