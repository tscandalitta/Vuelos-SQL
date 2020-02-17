#Archivo batch (vuelos.sql)

# Creacion de la Base de Datos
CREATE DATABASE vuelos;

# selecciono la base de datos
USE vuelos;

#-------------------------------------------------------------------------
# Creacion Tablas para las entidades

CREATE TABLE ubicaciones (
  pais VARCHAR(45) NOT NULL,
  estado VARCHAR(45) NOT NULL,
  ciudad VARCHAR(45) NOT NULL,
  huso SMALLINT NOT NULL CHECK (huso BETWEEN -12 AND 12),

  CONSTRAINT pk_ubicaciones
  PRIMARY KEY(pais, estado, ciudad)
) ENGINE=InnoDB;

CREATE TABLE aeropuertos (
  codigo VARCHAR(45) NOT NULL,
  nombre VARCHAR(45) NOT NULL,
  telefono VARCHAR(45) NOT NULL,
  direccion VARCHAR(45) NOT NULL,
  pais VARCHAR(45) NOT NULL,
  estado VARCHAR(45) NOT NULL,
  ciudad VARCHAR(45) NOT NULL,

  CONSTRAINT pk_codigoAeropuerto
  PRIMARY KEY (codigo),

  CONSTRAINT FK_ubicacion
  FOREIGN KEY (pais,estado,ciudad) REFERENCES ubicaciones (pais,estado,ciudad)
) ENGINE=InnoDB;

CREATE TABLE vuelos_programados (
  numero VARCHAR(45) NOT NULL ,
  aeropuerto_salida VARCHAR(45) NOT NULL,
  aeropuerto_llegada VARCHAR(45) NOT NULL,

  CONSTRAINT pk_numero
  PRIMARY KEY (numero),

  CONSTRAINT FK_aeropuerto_salida
  FOREIGN KEY (aeropuerto_salida) REFERENCES aeropuertos (codigo),

  CONSTRAINT FK_aeropuerto_llegada
  FOREIGN KEY (aeropuerto_llegada) REFERENCES aeropuertos (codigo)
) ENGINE=InnoDB;

CREATE TABLE modelos_avion(
  modelo VARCHAR(45) NOT NULL,
  fabricante VARCHAR(45) NOT NULL,
  cabinas INT UNSIGNED NOT NULL,
  cant_asientos INT UNSIGNED NOT NULL,

  CONSTRAINT pk_modelo
  PRIMARY KEY (modelo)
) ENGINE=InnoDB;

CREATE TABLE salidas (
  vuelo VARCHAR(45) NOT NULL,
  dia ENUM('Do','Lu','Ma','Mi','Ju','Vi','Sa'),
  modelo_avion VARCHAR(45) NOT NULL,
  hora_sale TIME(0) NOT NULL,
  hora_llega TIME(0) NOT NULL,

  CONSTRAINT pk_salidas
  PRIMARY KEY (vuelo,dia),

  CONSTRAINT FK_salidas_vuelos_programados
  FOREIGN KEY (vuelo) REFERENCES vuelos_programados (numero),

  CONSTRAINT FK_salidas_modelos_avion
  FOREIGN KEY (modelo_avion) REFERENCES modelos_avion (modelo)
) ENGINE=InnoDB;

CREATE TABLE instancias_vuelo (
  vuelo VARCHAR(45) NOT NULL,
  fecha DATE NOT NULL,
  dia ENUM ('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL,
  estado VARCHAR(45) ,

  CONSTRAINT pk_instancia
  PRIMARY KEY (vuelo,fecha),

  CONSTRAINT fk_instancia
  FOREIGN KEY (vuelo,dia) REFERENCES salidas(vuelo,dia)
) ENGINE=InnoDB;

CREATE TABLE clases (
 nombre VARCHAR(20) NOT NULL,
 porcentaje DECIMAL(2,2) UNSIGNED NOT NULL CHECK(porcentaje BETWEEN 0.00 and 0.99),

 CONSTRAINT pk_clases
 PRIMARY KEY (nombre)
) ENGINE=InnoDB;

CREATE TABLE comodidades (
  codigo INT UNSIGNED NOT NULL,
  descripcion TEXT(45) NOT NULL,

  CONSTRAINT pk_comodidades
  PRIMARY KEY (codigo)
) ENGINE=InnoDB;

CREATE TABLE pasajeros (
  doc_tipo VARCHAR(3) NOT NULL,
  doc_nro INT UNSIGNED NOT NULL,
  apellido VARCHAR(45) NOT NULL,
  nombre VARCHAR(45) NOT NULL,
  direccion VARCHAR(45) NOT NULL,
  telefono VARCHAR(45) NOT NULL,
  nacionalidad VARCHAR(45) NOT NULL,

  CONSTRAINT pk_pasajeros
  PRIMARY KEY (doc_tipo, doc_nro)
) ENGINE=InnoDB;

CREATE TABLE empleados (
  legajo INT UNSIGNED NOT NULL,
  doc_tipo VARCHAR(3) NOT NULL,
  doc_nro INT UNSIGNED NOT NULL,
  apellido VARCHAR(45) NOT NULL,
  nombre VARCHAR(45) NOT NULL,
  direccion VARCHAR(45) NOT NULL,
  telefono VARCHAR(45) NOT NULL,
  password VARCHAR(32) NOT NULL,

 CONSTRAINT pk_empleados
 PRIMARY KEY (legajo)
) ENGINE=InnoDB;

CREATE TABLE reservas (
  numero INT UNSIGNED NOT NULL AUTO_INCREMENT,
  fecha DATE NOT NULL,
  vencimiento DATE NOT NULL,
  estado VARCHAR(20) NOT NULL,
  doc_tipo VARCHAR(3) NOT NULL,
  doc_nro INT UNSIGNED NOT NULL,
  legajo INT UNSIGNED NOT NULL,

 CONSTRAINT pk_reservas
 PRIMARY KEY (numero),

 CONSTRAINT fk_reservas_empleados
 FOREIGN KEY (legajo) REFERENCES empleados (legajo),

 CONSTRAINT fk_reservas_pasajeros
 FOREIGN KEY (doc_tipo, doc_nro) REFERENCES pasajeros (doc_tipo, doc_nro)
) ENGINE=InnoDB;

#-------------------------------------------------------------------------
# Creacion Tablas para las relaciones

CREATE TABLE brinda (
  vuelo VARCHAR(45) NOT NULL,
  dia ENUM('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'),
  clase VARCHAR(20) NOT NULL,
  precio DECIMAL(7,2) UNSIGNED NOT NULL CHECK(PRECIO > 0),
  cant_asientos INT UNSIGNED NOT NULL,

  CONSTRAINT pk_brinda
  PRIMARY KEY (vuelo, dia, clase),

  CONSTRAINT fk_brinda_salidas
  FOREIGN KEY (vuelo, dia) REFERENCES salidas (vuelo, dia),

  CONSTRAINT fk_brinda_clases
  FOREIGN KEY (clase) REFERENCES clases (nombre)
) ENGINE=InnoDB;

CREATE TABLE posee (
  clase VARCHAR(20) NOT NULL,
  comodidad INT UNSIGNED NOT NULL,

  CONSTRAINT pk_posee
  PRIMARY KEY (clase, comodidad),

  CONSTRAINT fk_posee_clases
  FOREIGN KEY (clase) REFERENCES clases (nombre),

  CONSTRAINT fk_posee_comodidades
  FOREIGN KEY (comodidad) REFERENCES comodidades (codigo)
) ENGINE=InnoDB;

CREATE TABLE reserva_vuelo_clase (
  numero INT UNSIGNED NOT NULL ,
  vuelo VARCHAR(45) NOT NULL,
  fecha_vuelo DATE NOT NULL,
  clase VARCHAR(20) NOT NULL,

  CONSTRAINT pk_reserva_vuelo_clase
  PRIMARY KEY (numero, vuelo, fecha_vuelo),

  CONSTRAINT fk_reserva_vuelo_clase_reservas
  FOREIGN KEY (numero) REFERENCES reservas (numero),

  CONSTRAINT fk_reserva_vuelo_clase_instancias_vuelo
  FOREIGN KEY (vuelo, fecha_vuelo) REFERENCES instancias_vuelo (vuelo, fecha),

  CONSTRAINT fk_reserva_vuelo_clase_clases
  FOREIGN KEY (clase) REFERENCES clases (nombre)
) ENGINE=InnoDB;

CREATE TABLE asientos_reservados (
  cantidad INT UNSIGNED NOT NULL,
  clase VARCHAR(20) NOT NULL,
  vuelo VARCHAR(45) NOT NULL,
  fecha DATE NOT NULL,

  CONSTRAINT pk_asientos_reservados
  PRIMARY KEY (vuelo, fecha, clase),

  CONSTRAINT fk_asientos_disponibles_clase
  FOREIGN KEY (clase) REFERENCES clases(nombre),

  CONSTRAINT fk_asientos_disponibles_instancia_vuelo
  FOREIGN KEY (vuelo, fecha) REFERENCES instancias_vuelo(vuelo,fecha)
) ENGINE=InnoDB;

#-------------------------------------------------------------------------
# Creacion de vistas

CREATE VIEW info_vuelo AS

  SELECT DISTINCT   iv.vuelo, iv.fecha, iv.dia, s.hora_sale, s.hora_llega, s.modelo_avion,
            (SELECT IF (TIMEDIFF(s.hora_llega,s.hora_sale) < 0,
                        (SELECT ADDTIME(TIMEDIFF(s.hora_llega,s.hora_sale),'24:00:00')),
                        TIMEDIFF(s.hora_llega,s.hora_sale))) as duracion,
            vp.aeropuerto_salida, asalida.nombre as 'aerop_salida', asalida.ciudad as 'ciudad_salida',
            asalida.estado as 'estado_salida', asalida.pais as 'pais_salida', vp.aeropuerto_llegada,
            allegada.nombre as 'aerop_llegada', allegada.ciudad as 'ciudad_llegada',
            allegada.estado as 'estado_llegada', allegada.pais as 'pais_llegada'

  FROM      (instancias_vuelo as iv JOIN salidas as s JOIN vuelos_programados as vp JOIN
            aeropuertos as asalida JOIN aeropuertos as allegada)

  WHERE     (iv.vuelo = s.vuelo) AND (iv.dia = s.dia) AND (vp.numero = s.vuelo) AND
            (vp.aeropuerto_salida = asalida.codigo) AND (vp.aeropuerto_llegada = allegada.codigo);


CREATE VIEW info_vuelo2 AS

  SELECT  t_disponibles.vuelo, t_disponibles.fecha, t_disponibles.dia, t_disponibles.clase, precio,
  		    FLOOR(cant_asientos*(1+porcentaje)) as cant_asientos,
  			  IFNULL(FLOOR(cant_asientos*(1+porcentaje)-vendidos),
                 FLOOR(cant_asientos*(1+porcentaje)) ) as asientos_disponibles

  FROM    (SELECT iv.vuelo, iv.fecha, iv.dia, b.clase, precio, cant_asientos, porcentaje
  			   FROM instancias_vuelo as iv, brinda as b, clases as c
  			   WHERE (iv.vuelo = b.vuelo) AND (iv.dia = b.dia) AND (b.clase = c.nombre) ) as t_disponibles

  		     LEFT JOIN

  			  (SELECT vuelo, fecha_vuelo, clase, count(clase) as vendidos
  			   FROM reserva_vuelo_clase as rvc
  			   GROUP BY vuelo,fecha_vuelo,clase) as t_vendidos

  ON      (t_disponibles.vuelo = t_vendidos.vuelo) AND (t_disponibles.fecha = t_vendidos.fecha_vuelo)
  		    AND (t_disponibles.clase = t_vendidos.clase);


 CREATE VIEW vuelos_disponibles AS

   SELECT DISTINCT  inf1.vuelo as 'nro_vuelo', inf1.fecha as 'fecha', inf1.dia as 'dia',
            inf1.hora_sale as 'salida', inf1.hora_llega as 'llegada', inf1.modelo_avion as 'avion',
            inf1.duracion as 'duracion', inf1.aeropuerto_salida as 'aeropuerto_origen', inf1.aerop_salida as 'nombre_origen',
            inf1.ciudad_salida as 'ciudad_origen', inf1.estado_salida as 'estado_origen',
            inf1.pais_salida as 'pais_origen', inf1.aeropuerto_llegada as 'aeropuerto_destino',
            inf1.aerop_llegada as 'nombre_destino', inf1.ciudad_llegada as 'ciudad_destino',
            inf1.estado_llegada as 'estado_destino', inf1.pais_llegada as 'pais_destino',
            inf2.clase as 'clase', inf2.precio as 'precio', inf2.asientos_disponibles as 'asientos_disponibles'

   FROM info_vuelo as inf1 JOIN info_vuelo2 as inf2

   WHERE (inf1.vuelo = inf2.vuelo AND inf1.fecha = inf2.fecha AND inf1.dia = inf2.dia);

#-------------------------------------------------------------------------
# Creacion de stored procedures

# Defino '!' como delimitador de sentencias
delimiter !

# Procedimiento para realizar una reserva de un vuelo de ida
CREATE PROCEDURE reservar_ida(IN numero VARCHAR(45), IN fecha DATE, IN clase VARCHAR(20),
                              IN tipo_doc VARCHAR(3), IN nro_doc INT, IN legajo_empleado INT)

BEGIN
    DECLARE cant_reservados SMALLINT;
    DECLARE estado_reserva VARCHAR(20);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
		BEGIN
			SELECT 'SQLEXCEPTION!, transaccion abortada' AS resultado;
			ROLLBACK;
		END;

    START TRANSACTION;
        # Se verifica que los datos sean correctos
        IF  EXISTS (SELECT * FROM pasajeros WHERE doc_nro = nro_doc AND doc_tipo = tipo_doc)
        THEN
            IF EXISTS (SELECT * FROM empleados WHERE legajo = legajo_empleado)
            THEN
                IF  EXISTS (SELECT * FROM instancias_vuelo as iv WHERE iv.vuelo = numero AND iv.fecha = fecha) AND
                    EXISTS (SELECT * FROM instancias_vuelo as iv JOIN brinda as b WHERE (iv.fecha = fecha) AND
                        (iv.vuelo = numero) AND (iv.vuelo = b.vuelo) AND (iv.dia = b.dia) AND (b.clase = clase))

                THEN
                    SELECT cantidad INTO cant_reservados FROM asientos_reservados ar WHERE ar.vuelo = numero AND
                                                        ar.fecha = fecha AND ar.clase = clase FOR UPDATE;
                    # Se verifica que el vuelo seleccionado tenga asientos disponibles en una clase y fecha dada
                    IF EXISTS (SELECT * FROM vuelos_disponibles as vd WHERE vd.nro_vuelo = numero AND vd.fecha = fecha AND
                                                        vd.clase = clase AND vd.asientos_disponibles > 0)
                    THEN
                        # Si la capacidad fisica está colmada, la reserva queda en espera, en caso contrario queda confirmada

                        #Si no hay datos cargados en asientos_resevados, cant_reservados es empty set y da falso cuando tendria
                        # que dar verdadero
                        SELECT IF(cant_reservados < cant_asientos, 'confirmada', 'en espera') INTO estado_reserva
                    	FROM instancias_vuelo as iv JOIN brinda as b
                    	WHERE (iv.fecha = fecha) AND (iv.vuelo = numero) AND (iv.vuelo = b.vuelo) AND
                              (iv.dia = b.dia) AND (b.clase = clase);

                        # Se inserta la reserva y el vuelo en las tablas correspondientes
                        INSERT INTO reservas(fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES
                            (CURDATE(), DATE_SUB(fecha, INTERVAL 15 DAY), estado_reserva, tipo_doc, nro_doc, legajo_empleado);
                        INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(), numero, fecha, clase);

                        UPDATE asientos_reservados as ar SET cantidad = cantidad + 1 WHERE (ar.vuelo = numero) AND
                                                                                (ar.fecha = fecha) AND (ar.clase = clase);

                        SELECT 'La reserva se ha realizado con exito' as resultado;
                    ELSE
        	            SELECT 'No hay lugares disponibles en el vuelo y clase solicitados' as resultado;
                    END IF;
                ELSE
                    SELECT 'No existe el vuelo en el dia y clase solicitados' as resultado;
                END IF;
            ELSE
                SELECT 'No se encuentra el empleado' as resultado;
            END IF;
        ELSE
			SELECT 'No existe el pasajero indicado' AS resultado;
		END IF;
	COMMIT;
END;!

# Procedimiento para realizar una reserva de un vuelo de ida y uno de vuelta
CREATE PROCEDURE reservar_ida_vuelta(IN nro_ida VARCHAR(45),IN nro_vuelta VARCHAR(45), IN fecha_ida DATE,
                        IN fecha_vuelta DATE, IN clase_ida VARCHAR(20),IN clase_vuelta VARCHAR(20),
                        IN tipo_doc VARCHAR(3), IN nro_doc INT, IN legajo_empleado INT)

BEGIN

    DECLARE estado_reserva VARCHAR(20);
    DECLARE cant_reservados_ida SMALLINT;
    DECLARE cant_reservados_vuelta SMALLINT;

        START TRANSACTION;
            # Se verifica que los datos sean correctos
            IF  EXISTS (SELECT * FROM pasajeros WHERE doc_nro = nro_doc AND doc_tipo = tipo_doc)
            THEN
                IF EXISTS (SELECT * FROM empleados WHERE legajo = legajo_empleado)
                THEN
                    IF  EXISTS (SELECT * FROM instancias_vuelo as iv WHERE iv.vuelo = nro_ida AND iv.fecha = fecha_ida) AND
        			    EXISTS (SELECT * FROM instancias_vuelo as iv WHERE iv.vuelo = nro_vuelta AND iv.fecha = fecha_vuelta) AND
                        EXISTS (SELECT * FROM instancias_vuelo as iv, brinda as b WHERE iv.fecha = fecha_ida AND
                                    iv.vuelo = nro_ida AND iv.vuelo = b.vuelo AND iv.dia = b.dia AND b.clase = clase_ida) AND
            			EXISTS (SELECT * FROM instancias_vuelo as iv, brinda as b WHERE iv.fecha = fecha_vuelta AND
                                    iv.vuelo = nro_vuelta AND iv.vuelo = b.vuelo AND iv.dia = b.dia AND b.clase = clase_vuelta)
                    THEN
                        SELECT cantidad INTO cant_reservados_ida FROM asientos_reservados as ar WHERE ar.vuelo = nro_ida AND
                                                                            ar.fecha = fecha_ida AND ar.clase = clase_ida FOR UPDATE;
                        SELECT cantidad INTO cant_reservados_vuelta FROM asientos_reservados as ar WHERE ar.vuelo = nro_vuelta AND
                                                                        ar.fecha = fecha_vuelta AND ar.clase = clase_vuelta FOR UPDATE;

                        # Se verifica que el vuelo de ida tenga asientos disponibles
                        IF EXISTS (SELECT * FROM vuelos_disponibles as vd WHERE vd.nro_vuelo = nro_ida AND vd.fecha = fecha_ida AND
                                                                        vd.clase = clase_ida AND asientos_disponibles > 0)
                        THEN
                            # Se verifica que el vuelo de vuelta tenga asientos disponibles
                            IF EXISTS (SELECT * FROM vuelos_disponibles as vd WHERE vd.nro_vuelo = nro_vuelta AND vd.fecha = fecha_vuelta AND
                                                                        vd.clase = clase_vuelta AND asientos_disponibles > 0)
                            THEN
                                SELECT IF(cant_reservados_ida < cant_asientos, 'confirmada', 'en espera') INTO estado_reserva
                                FROM instancias_vuelo NATURAL JOIN brinda
                                WHERE (vuelo = nro_ida) AND (fecha = fecha_ida) AND (clase = clase_ida);

                                IF (estado_reserva = 'confirmada')
                                THEN
                                    SELECT IF(cant_reservados_vuelta < cant_asientos, 'confirmada', 'en espera') INTO estado_reserva
                                    FROM instancias_vuelo NATURAL JOIN brinda
                                    WHERE (vuelo = nro_vuelta) AND (fecha = fecha_vuelta) AND (clase = clase_vuelta);
                                END IF;

                                # Se inserta la reserva y los vuelos en las tablas correspondientes
                                INSERT INTO reservas(fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES
                                    (CURDATE(), DATE_SUB(fecha_ida, INTERVAL 15 DAY), estado_reserva, tipo_doc, nro_doc, legajo_empleado);
                                INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(), nro_ida, fecha_ida, clase_ida);
                                INSERT INTO reserva_vuelo_clase VALUES (LAST_INSERT_ID(), nro_vuelta, fecha_vuelta, clase_vuelta);

                                UPDATE asientos_reservados SET cantidad = cantidad + 1 WHERE vuelo = nro_ida AND
                                                                            fecha = fecha_ida AND clase = clase_ida;
                                UPDATE asientos_reservados SET cantidad = cantidad + 1 WHERE vuelo = nro_vuelta AND
                                                                            fecha = fecha_vuelta AND clase = clase_vuelta;

                                SELECT 'La reserva se ha realizado con exito' as resultado;
                            ELSE
                                SELECT 'No hay lugares disponibles para el vuelo de vuelta en la clase solicitada' as resultado;
                            END IF;
                        ELSE
                            SELECT 'No hay lugares disponibles para el vuelo de ida en la clase solicitada' as resultado;
                        END IF;
                    ELSE
                        SELECT 'Datos de vuelos incorrectos' as resultado;
                    END IF;
                ELSE
                    SELECT 'No se encuentra el empleado' as resultado;
                END IF;
            ELSE
    			SELECT 'No existe el pasajero indicado' AS resultado;
    		END IF;
    	COMMIT;
    END;!

delimiter ;

#-------------------------------------------------------------------------
# Creacion de usuarios

CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON vuelos.* TO 'admin'@'localhost' WITH GRANT OPTION;

CREATE USER 'empleado'@'%' IDENTIFIED BY 'empleado';
GRANT SELECT ON vuelos.* TO 'empleado';
GRANT INSERT,UPDATE,DELETE ON vuelos.reservas TO 'empleado';
GRANT INSERT,UPDATE,DELETE ON vuelos.pasajeros TO 'empleado';
GRANT INSERT,UPDATE,DELETE ON vuelos.reserva_vuelo_clase TO 'empleado';
GRANT EXECUTE ON  vuelos.* TO 'empleado';

CREATE USER 'cliente'@'%' IDENTIFIED BY 'cliente';
GRANT SELECT ON vuelos.vuelos_disponibles TO 'cliente';
