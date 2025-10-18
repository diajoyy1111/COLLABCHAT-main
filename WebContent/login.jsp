<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - CollabChat</title>
    <link rel="stylesheet" href="styles.css" />
</head>
<body>
    <div class="container" style="max-width: 500px; margin-top: 100px;">
        <div style="text-align: center; margin-bottom: 30px;">
            <h1 style="color: #2c3e50; font-size: 32px; margin-bottom: 10px;">🎓 CollabChat</h1>
            <p style="color: #6c757d; font-size: 16px;">Classroom Collaboration Platform</p>
        </div>
        
        <form method="post" action="login" style="margin-bottom: 20px;">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" placeholder="Enter your username" required />
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required />
            </div>
            
            <button type="submit" style="width: 100%; padding: 15px; font-size: 16px; margin-top: 10px;">
                Login to Classroom
            </button>
        </form>
        
        <div style="text-align: center;">
            <p style="color: #6c757d; margin-bottom: 15px;">Don't have an account?</p>
            <a href="register.jsp" style="color: #3498db; text-decoration: none; font-weight: 600; padding: 10px 20px; border: 2px solid #3498db; border-radius: 25px; display: inline-block; transition: all 0.3s ease;">
                Create Account
            </a>
        </div>
        
        <div style="margin-top: 30px; padding: 20px; background: #f8f9fa; border-radius: 10px; text-align: center;">
            <h3 style="color: #2c3e50; margin-bottom: 10px;">Demo Credentials</h3>
            <p style="color: #6c757d; font-size: 14px; margin-bottom: 5px;">Username: <strong>demo</strong></p>
            <p style="color: #6c757d; font-size: 14px;">Password: <strong>demo123</strong></p>
        </div>
    </div>
    
    <script>
        // Add some interactive effects
        document.addEventListener('DOMContentLoaded', function() {
            const inputs = document.querySelectorAll('input');
            inputs.forEach(input => {
                input.addEventListener('focus', function() {
                    this.parentElement.style.transform = 'scale(1.02)';
                });
                input.addEventListener('blur', function() {
                    this.parentElement.style.transform = 'scale(1)';
                });
            });
            
            // Auto-fill demo credentials on click
            const demoBox = document.querySelector('.alert-info');
            if (demoBox) {
                demoBox.addEventListener('click', function() {
                    document.getElementById('username').value = 'demo';
                    document.getElementById('password').value = 'demo123';
                });
            }
        });
    </script>
</body>
</html>
