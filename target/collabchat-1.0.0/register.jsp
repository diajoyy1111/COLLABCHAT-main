<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - CollabChat</title>
    <link rel="stylesheet" href="styles.css" />
</head>
<body>
    <div class="container" style="max-width: 500px; margin-top: 80px;">
        <div style="text-align: center; margin-bottom: 30px;">
            <h1 style="color: #2c3e50; font-size: 32px; margin-bottom: 10px;">🎓 CollabChat</h1>
            <p style="color: #6c757d; font-size: 16px;">Join the Classroom Collaboration Platform</p>
        </div>
        
        <form method="post" action="register" style="margin-bottom: 20px;">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" placeholder="Choose a username" required />
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Create a secure password" required />
            </div>
            
            <div class="form-group">
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm your password" required />
            </div>
            
            <button type="submit" style="width: 100%; padding: 15px; font-size: 16px; margin-top: 10px;">
                Create Account
            </button>
        </form>
        
        <div style="text-align: center;">
            <p style="color: #6c757d; margin-bottom: 15px;">Already have an account?</p>
            <a href="login.jsp" style="color: #3498db; text-decoration: none; font-weight: 600; padding: 10px 20px; border: 2px solid #3498db; border-radius: 25px; display: inline-block; transition: all 0.3s ease;">
                Login Here
            </a>
        </div>
        
        <div style="margin-top: 30px; padding: 20px; background: #d1ecf1; border-radius: 10px; text-align: center;">
            <h3 style="color: #0c5460; margin-bottom: 10px;">🔒 Security Note</h3>
            <p style="color: #0c5460; font-size: 14px;">Your password is securely encrypted using BCrypt hashing before storage.</p>
        </div>
    </div>
    
    <script>
        // Password confirmation validation
        document.addEventListener('DOMContentLoaded', function() {
            const password = document.getElementById('password');
            const confirmPassword = document.getElementById('confirmPassword');
            const form = document.querySelector('form');
            
            function validatePasswords() {
                if (password.value !== confirmPassword.value) {
                    confirmPassword.setCustomValidity('Passwords do not match');
                    confirmPassword.style.borderColor = '#e74c3c';
                } else {
                    confirmPassword.setCustomValidity('');
                    confirmPassword.style.borderColor = '#e9ecef';
                }
            }
            
            password.addEventListener('input', validatePasswords);
            confirmPassword.addEventListener('input', validatePasswords);
            
            // Form submission validation
            form.addEventListener('submit', function(e) {
                if (password.value !== confirmPassword.value) {
                    e.preventDefault();
                    alert('Passwords do not match!');
                    return false;
                }
                
                if (password.value.length < 6) {
                    e.preventDefault();
                    alert('Password must be at least 6 characters long!');
                    return false;
                }
            });
            
            // Add interactive effects
            const inputs = document.querySelectorAll('input');
            inputs.forEach(input => {
                input.addEventListener('focus', function() {
                    this.parentElement.style.transform = 'scale(1.02)';
                });
                input.addEventListener('blur', function() {
                    this.parentElement.style.transform = 'scale(1)';
                });
            });
        });
    </script>
</body>
</html>
