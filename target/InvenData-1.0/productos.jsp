<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>InvenData - Productos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">
    <h2>Inventario de Productos</h2>
    
    <button type="button" class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#modalProducto">
    Nuevo Producto
</button>

<div class="modal fade" id="modalProducto" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="ProductoServlet" method="POST">
                <div class="modal-header">
                    <h5 class="modal-title">Agregar Producto</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="text" name="txtNombre" placeholder="Nombre" class="form-control mb-2" required>
                    <input type="text" name="txtCategoria" placeholder="Categoría" class="form-control mb-2" required>
                    <input type="number" step="0.01" name="txtPrecio" placeholder="Precio" class="form-control mb-2" required>
                    <input type="number" name="txtStock" placeholder="Stock Inicial" class="form-control mb-2" required>
                    <input type="number" name="txtStockMin" placeholder="Stock Mínimo" class="form-control mb-2" required>
                </div>
                <div class="modal-footer">
                    <button type="submit" name="accion" value="guardar" class="btn btn-success">Guardar Producto</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <table class="table table-striped">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Categoría</th>
                <th>Precio</th>
                <th>Stock</th>
                <th>Estado</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="p" items="${productos}">
                <tr>
                    <td>${p.id}</td>
                    <td>${p.nombre}</td>
                    <td>${p.categoria}</td>
                    <td>$${p.precio}</td>
                    <td>
                        <span class="badge ${p.stock <= p.stockMinimo ? 'bg-danger' : 'bg-success'}">
                            ${p.stock}
                        </span>
                    </td>
                    <td>${p.estado}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>