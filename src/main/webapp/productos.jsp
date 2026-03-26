<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>InvenData - Gestión de Inventario</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
            <div class="container">
                <a class="navbar-brand" href="#">📊 InvenData</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item">
                            <a class="nav-link active" href="ProductoServlet">Inventario</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="MovimientoServlet">Historial</a>
                        </li>
                    </ul>
                    <div class="d-flex align-items-center">
                        <span class="navbar-text me-3 text-white">
                            Bienvenido, <strong>${sessionScope.usuarioLogueado.nombreCompleto}</strong>
                        </span>
                        <a href="LoginServlet?accion=logout" class="btn btn-outline-danger btn-sm">Cerrar Sesión</a>
                    </div>
                </div>
            </div>
        </nav>

        <div class="container">
            <h2 class="mb-4">📦 Panel de Inventario</h2>

            <c:if test="${param.error == 'insuficiente'}">
                <div class="alert alert-danger">❌ Error: Stock insuficiente para realizar la salida.</div>
            </c:if>
            <c:if test="${param.msj == 'exito'}">
                <div class="alert alert-success">✅ Movimiento registrado correctamente.</div>
            </c:if>

            <button type="button" class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#modalProducto">
                + Nuevo Producto
            </button>

            <table class="table table-hover table-bordered shadow-sm">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Precio</th>
                        <th>Stock Actual</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${productos}">
                        <tr>
                            <td>${p.id}</td>
                            <td>${p.nombre}</td>
                            <td>${p.categoria}</td>
                            <td>$ ${p.precio}</td>
                            <td>
                                <span class="badge ${p.stock <= p.stockMinimo ? 'bg-danger' : 'bg-success'}">
                                    ${p.stock} (Min: ${p.stockMinimo})
                                </span>
                            </td>
                            <td>${p.estado}</td>
                            <td>
                                <button type="button" class="btn btn-warning btn-sm" 
                                        onclick="prepararMovimiento(${p.id}, '${p.nombre}')" 
                                        data-bs-toggle="modal" data-bs-target="#modalMovimiento">
                                    ± Movimiento
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="modal fade" id="modalProducto" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="ProductoServlet" method="POST">
                        <div class="modal-header">
                            <h5 class="modal-title">Agregar Nuevo Producto</h5>
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

        <div class="modal fade" id="modalMovimiento" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="MovimientoServlet" method="POST">
                        <div class="modal-header bg-warning">
                            <h5 class="modal-title" id="tituloMov">Registrar Movimiento</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="txtIdProducto" id="idProdMov">
                            
                            <div class="mb-3">
                                <label class="form-label">Tipo de Movimiento</label>
                                <select name="txtTipo" class="form-select">
                                    <option value="ENTRADA">ENTRADA (Aumentar Stock)</option>
                                    <option value="SALIDA">SALIDA (Disminuir Stock)</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Cantidad</label>
                                <input type="number" name="txtCantidad" class="form-control" min="1" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-dark">Procesar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        
        <script>
            function prepararMovimiento(id, nombre) {
                document.getElementById('idProdMov').value = id;
                document.getElementById('tituloMov').innerText = "Movimiento para: " + nombre;
            }
        </script>
    </body>
</html>