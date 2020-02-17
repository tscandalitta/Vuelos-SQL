USE vuelos;

delimiter !

CREATE TRIGGER cargarAutomaticamenteInstanciaVuelo
AFTER INSERT ON salidas
FOR EACH ROW
    BEGIN
        DECLARE fecha_anio_dsp DATE;
        DECLARE fecha_inicial DATE;

        SELECT CURDATE() INTO fecha_inicial;
        SELECT DATE_ADD(CURDATE(), INTERVAL 1 YEAR) INTO fecha_anio_dsp;

        WHILE (fecha_inicial <= fecha_anio_dsp) DO
            IF (diaSemana(DAYOFWEEK(fecha_inicial)) = NEW.dia)
            THEN
                INSERT INTO instancias_vuelo VALUES (NEW.vuelo, fecha_inicial, NEW.dia, 'a tiempo');
            END IF;

            SELECT DATE_ADD(fecha_inicial, INTERVAL 1 DAY) INTO fecha_inicial;
        END WHILE;

    END ; !

    # Funcion que dado un numero de un dia de la semana, retorna el dia asociado.
    CREATE FUNCTION diaSemana(dia INT) RETURNS VARCHAR(2)
    DETERMINISTIC
        BEGIN
        	CASE dia
        		WHEN 1 THEN RETURN 'Do';
        		WHEN 2 THEN RETURN 'Lu';
        		WHEN 3 THEN RETURN 'Ma';
        		WHEN 4 THEN RETURN 'Mi';
        		WHEN 5 THEN RETURN 'Ju';
        		WHEN 6 THEN RETURN 'Vi';
        		WHEN 7 THEN RETURN 'Sa';
        	END CASE;
         END; !

delimiter ;
