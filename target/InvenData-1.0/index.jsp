<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>InvenData - Login</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body { background-color: #f4f7f6; }
            .login-card { max-width: 400px; margin: auto; margin-top: 100px; border-radius: 15px; }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="card login-card shadow-lg p-4">
                <div class="text-center mb-4">
                    <h2>🔐 InvenData</h2>
                    <p class="text-muted">Ingresa tus credenciales</p>
                </div>

                <% if(request.getParameter("error") != null) { %>
                    <div class="alert alert-danger text-center">
                        Usuario o contraseña incorrectos
                    </div>
                <% } %>

                <form action="LoginServlet" method="POST">
                    <div class="mb-3">
                        <label class="form-label">Nombre de Usuario</label>
                        <input type="text" name="txtUser" class="form-control" placeholder="Ej: admin" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Contraseña</label>
                        <input type="password" name="txtPass" class="form-control" placeholder="****" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100 py-2">Ingresar</button>
                </form>
            </div>
        </div>
    </body>
</html>