# Guia de compilación y pruebas de los micros Clientes y Cuentas/Movimientos

Este repositorio contiene dos microservicios independientes construidos con Java 21 y Spring Boot 3:

- `customers-service`: Gestión de Clientes
- `accounts-service`: Gestión de Cuentas/Movimientos y Reportes

Incluye además Dockerfiles, docker-compose, script SQL (`BaseDatos.sql`) y colección Postman (`postman_collection.json`).

## Requisitos Previos
- Java 21
- Maven
- Docker y Docker Compose
- Postman

## Pasos para Ejecutar la Aplicación
### 1. Compilación de Proyectos
Desde la raíz de cada microservicio, ejecutar:
``` bash
mvn -DskipTests clean package
```
en caso de fallo ejecutar desde la misma raiz de cada microservicio.

```
mvn -f customers-service/pom.xml clean package
mvn -f accounts-service/pom.xml clean package
```

### 2. Despliegue con Docker
Una vez generados los archivos .jar, desde la carpeta padre del proyecto ejecutar:

``` bash
docker compose up -d --build
```

- Servicios expuestos:
  - customers-service: http://localhost:8081
  - accounts-service: http://localhost:8082
  - PostgreSQL customers: localhost:5433
  - PostgreSQL accounts: localhost:5434

## Pruebas con Postman
### 1. Importar Colección
1. Abrir Postman
2. Click en "Import"
3. Arrastrar el archivo `postman_collection.json` o seleccionarlo desde el explorador
4. Confirmar la importación

### 2. Flujo de Pruebas
#### Paso 1: Crear Cliente
1. Ejecutar el request "1.1 Crear Cliente"
2. Copiar el `clientId` de la respuesta (lo necesitarás para los siguientes pasos)

#### Paso 2: Crear Cuenta
1. Ir al request "2.1 Crear Cuenta"
2. En el body del request, reemplazar el campo `clientId` con el ID copiado del paso anterior
3. Ejecutar el request
4. Copiar el `id` de la cuenta de la respuesta

#### Paso 3: Crear Movimientos
1. Ir al request "3.1 Crear Movimiento"
2. Reemplazar el `accountId` con el ID de la cuenta copiado
3. Ejecutar el request1.
  - Para probar diferentes escenarios, puedes modificar:
    - `amount`: monto del movimiento
    - `movementType`: "DEPOSIT" para depósitos, "RETIREMENT" para retiros

#### Paso 4: Consultar Información
Puedes usar los siguientes endpoints para verificar la información:
- "1.2 Obtener Cliente": usar el `clientId`
- "2.2 Obtener Cuenta": usar el `accountId`
- "3.2 Obtener Movimientos por Cuenta": usar el `accountId`
- "3.3 Obtener Movimientos por Rango": usar el `accountId` y ajustar las fechas según necesites
- "3.4 Ver Estado de Cuenta": usar el `clientId` y ajustar el rango de fechas


