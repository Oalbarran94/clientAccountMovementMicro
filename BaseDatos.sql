CREATE TABLE clientes (
                          cliente_id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          genre VARCHAR(255) NOT NULL,
                          age INTEGER NOT NULL CHECK (age >= 0),
                          identification VARCHAR(255) NOT NULL UNIQUE,
                          address VARCHAR(255) NOT NULL,
                          phone VARCHAR(255) NOT NULL,
                          state BOOLEAN NOT NULL
);

CREATE TABLE cuentas (
                         id UUID PRIMARY KEY,
                         numero_cuenta VARCHAR(255) NOT NULL UNIQUE,
                         tipo_cuenta VARCHAR(255) NOT NULL,
                         saldo_inicial DECIMAL(19,2) NOT NULL,
                         estado BOOLEAN NOT NULL,
                         cliente_id UUID NOT NULL REFERENCES clientes(cliente_id)
);

CREATE TABLE movimientos (
                             id UUID PRIMARY KEY,
                             cuenta_id UUID NOT NULL REFERENCES cuentas(id),
                             fecha TIMESTAMP WITH TIME ZONE NOT NULL,
                             tipo_movimiento VARCHAR(255) NOT NULL,
                             valor DECIMAL(19,2) NOT NULL,
                             saldo DECIMAL(19,2) NOT NULL
);

-- Crear índices
CREATE INDEX idx_cuenta_numero ON cuentas(numero_cuenta);
CREATE INDEX idx_cuenta_cliente ON cuentas(cliente_id);
CREATE INDEX idx_mov_cuenta ON movimientos(cuenta_id);
CREATE INDEX idx_mov_fecha ON movimientos(fecha);