-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 28-10-2025 a las 14:18:15
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `agenda_personal`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_actualizar_evento` (IN `p_id_evento` INT, IN `p_titulo` VARCHAR(200), IN `p_descripcion` TEXT, IN `p_fecha_inicio` DATETIME, IN `p_fecha_fin` DATETIME, IN `p_ubicacion` VARCHAR(200), IN `p_recordatorio` BOOLEAN, IN `p_estado` ENUM('pendiente','completado','cancelado'), IN `p_categorias` VARCHAR(500))   BEGIN
    DECLARE v_id_categoria INT;
    DECLARE v_pos INT;
    DECLARE v_categorias_temp VARCHAR(500);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error: No se pudo actualizar el evento' as mensaje, 0 as id_evento;
    END;
    
    START TRANSACTION;
    
    -- Actualizar evento
    UPDATE eventos 
    SET titulo = p_titulo,
        descripcion = p_descripcion,
        fecha_inicio = p_fecha_inicio,
        fecha_fin = p_fecha_fin,
        ubicacion = p_ubicacion,
        recordatorio = p_recordatorio,
        estado = p_estado
    WHERE id_evento = p_id_evento;
    
    -- Eliminar categorías anteriores
    DELETE FROM evento_categoria WHERE id_evento = p_id_evento;
    
    -- Agregar nuevas categorías
    IF p_categorias IS NOT NULL AND p_categorias != '' THEN
        SET v_categorias_temp = CONCAT(p_categorias, ',');
        
        WHILE LENGTH(v_categorias_temp) > 0 DO
            SET v_pos = LOCATE(',', v_categorias_temp);
            SET v_id_categoria = CAST(SUBSTRING(v_categorias_temp, 1, v_pos - 1) AS UNSIGNED);
            
            INSERT IGNORE INTO evento_categoria (id_evento, id_categoria)
            VALUES (p_id_evento, v_id_categoria);
            
            SET v_categorias_temp = SUBSTRING(v_categorias_temp, v_pos + 1);
        END WHILE;
    END IF;
    
    COMMIT;
    
    SELECT 'Evento actualizado exitosamente' as mensaje, p_id_evento as id_evento;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_cambiar_estado_evento` (IN `p_id_evento` INT, IN `p_estado` ENUM('pendiente','completado','cancelado'))   BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error: No se pudo cambiar el estado' as mensaje, 0 as id_evento;
    END;
    
    START TRANSACTION;
    
    UPDATE eventos SET estado = p_estado WHERE id_evento = p_id_evento;
    
    COMMIT;
    
    SELECT 'Estado actualizado exitosamente' as mensaje, p_id_evento as id_evento, p_estado as nuevo_estado;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_crear_evento` (IN `p_id_usuario` INT, IN `p_titulo` VARCHAR(200), IN `p_descripcion` TEXT, IN `p_fecha_inicio` DATETIME, IN `p_fecha_fin` DATETIME, IN `p_ubicacion` VARCHAR(200), IN `p_recordatorio` BOOLEAN, IN `p_categorias` VARCHAR(500))   BEGIN
    DECLARE v_id_evento INT;
    DECLARE v_id_categoria INT;
    DECLARE v_pos INT;
    DECLARE v_categorias_temp VARCHAR(500);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error: No se pudo crear el evento' as mensaje, 0 as id_evento;
    END;
    
    START TRANSACTION;
    
    -- Insertar evento
    INSERT INTO eventos (id_usuario, titulo, descripcion, fecha_inicio, fecha_fin, ubicacion, recordatorio)
    VALUES (p_id_usuario, p_titulo, p_descripcion, p_fecha_inicio, p_fecha_fin, p_ubicacion, p_recordatorio);
    
    SET v_id_evento = LAST_INSERT_ID();
    
    -- Procesar categorías (si se enviaron)
    IF p_categorias IS NOT NULL AND p_categorias != '' THEN
        SET v_categorias_temp = CONCAT(p_categorias, ',');
        
        WHILE LENGTH(v_categorias_temp) > 0 DO
            SET v_pos = LOCATE(',', v_categorias_temp);
            SET v_id_categoria = CAST(SUBSTRING(v_categorias_temp, 1, v_pos - 1) AS UNSIGNED);
            
            -- Insertar relación evento-categoría
            INSERT IGNORE INTO evento_categoria (id_evento, id_categoria)
            VALUES (v_id_evento, v_id_categoria);
            
            SET v_categorias_temp = SUBSTRING(v_categorias_temp, v_pos + 1);
        END WHILE;
    END IF;
    
    COMMIT;
    
    -- Devolver el ID del evento creado
    SELECT 'Evento creado exitosamente' as mensaje, v_id_evento as id_evento;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_crear_o_obtener_categoria` (IN `p_nombre` VARCHAR(50), IN `p_id_usuario` INT, OUT `p_id_categoria` INT)   BEGIN
    -- Buscar si ya existe la categoría
    SELECT id_categoria INTO p_id_categoria
    FROM categorias
    WHERE nombre = p_nombre
    LIMIT 1;
    
    -- Si no existe, crearla
    IF p_id_categoria IS NULL THEN
        INSERT INTO categorias (nombre) VALUES (p_nombre);
        SET p_id_categoria = LAST_INSERT_ID();
    END IF;
    
    -- Asociar la categoría al usuario si no está asociada
    INSERT IGNORE INTO usuario_categoria (id_usuario, id_categoria)
    VALUES (p_id_usuario, p_id_categoria);
    
    -- Devolver el ID de la categoría
    SELECT p_id_categoria as id_categoria;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_eliminar_evento` (IN `p_id_evento` INT)   BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SELECT 'Error: No se pudo eliminar el evento' as mensaje, 0 as id_evento_eliminado;
    END;
    
    START TRANSACTION;
    
    DELETE FROM eventos WHERE id_evento = p_id_evento;
    
    COMMIT;
    
    SELECT 'Evento eliminado exitosamente' as mensaje, p_id_evento as id_evento_eliminado;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id_categoria` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id_categoria`, `nombre`, `fecha_creacion`) VALUES
(1, 'Trabajo', '2025-10-28 12:53:38'),
(2, 'Personal', '2025-10-28 12:53:38'),
(3, 'Salud', '2025-10-28 12:53:38'),
(4, 'Familia', '2025-10-28 12:53:38');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `eventos`
--

CREATE TABLE `eventos` (
  `id_evento` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `fecha_inicio` datetime NOT NULL,
  `fecha_fin` datetime DEFAULT NULL,
  `ubicacion` varchar(200) DEFAULT NULL,
  `recordatorio` tinyint(1) DEFAULT 0,
  `estado` enum('pendiente','completado','cancelado') DEFAULT 'pendiente',
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `eventos`
--

INSERT INTO `eventos` (`id_evento`, `id_usuario`, `titulo`, `descripcion`, `fecha_inicio`, `fecha_fin`, `ubicacion`, `recordatorio`, `estado`, `fecha_creacion`) VALUES
(1, 1, 'Reunión de equipo', 'Revisión semanal del proyecto', '2025-10-28 10:00:00', '2025-10-28 11:00:00', 'Sala de conferencias', 1, 'pendiente', '2025-10-28 12:53:39'),
(2, 1, 'Cita médica', 'Chequeo anual', '2025-10-29 15:00:00', '2025-10-29 16:00:00', 'Hospital Central', 1, 'pendiente', '2025-10-28 12:53:39'),
(3, 2, 'Cumpleaños de Ana', 'Celebración familiar', '2025-11-05 18:00:00', '2025-11-05 22:00:00', 'Casa', 1, 'pendiente', '2025-10-28 12:53:39');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `evento_categoria`
--

CREATE TABLE `evento_categoria` (
  `id_evento` int(11) NOT NULL,
  `id_categoria` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `evento_categoria`
--

INSERT INTO `evento_categoria` (`id_evento`, `id_categoria`) VALUES
(1, 1),
(2, 3),
(3, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL,
  `nombre_usuario` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nombre_completo` varchar(100) DEFAULT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  `ultimo_acceso` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `nombre_usuario`, `email`, `password`, `nombre_completo`, `fecha_registro`, `ultimo_acceso`) VALUES
(1, 'jperez', 'jperez@email.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Juan Pérez', '2025-10-28 12:53:38', NULL),
(2, 'mgarcia', 'mgarcia@email.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'María García', '2025-10-28 12:53:38', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_categoria`
--

CREATE TABLE `usuario_categoria` (
  `id_usuario` int(11) NOT NULL,
  `id_categoria` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `usuario_categoria`
--

INSERT INTO `usuario_categoria` (`id_usuario`, `id_categoria`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(2, 4);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_categorias`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_categorias` (
`id_categoria` int(11)
,`nombre` varchar(50)
,`fecha_creacion` timestamp
,`usuarios_usando` bigint(21)
,`total_eventos` bigint(21)
,`usuarios` mediumtext
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_categorias_usuario`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_categorias_usuario` (
`id_usuario` int(11)
,`usuario` varchar(100)
,`id_categoria` int(11)
,`categoria` varchar(50)
,`eventos_en_categoria` bigint(21)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_eventos`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_eventos` (
`id_evento` int(11)
,`titulo` varchar(200)
,`descripcion` text
,`fecha_inicio` varchar(21)
,`fecha_fin` varchar(21)
,`ubicacion` varchar(200)
,`recordatorio` varchar(4)
,`estado` enum('pendiente','completado','cancelado')
,`usuario` varchar(100)
,`categorias` mediumtext
);

-- --------------------------------------------------------

--
-- Estructura para la vista `v_categorias`
--
DROP TABLE IF EXISTS `v_categorias`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_categorias`  AS SELECT `c`.`id_categoria` AS `id_categoria`, `c`.`nombre` AS `nombre`, `c`.`fecha_creacion` AS `fecha_creacion`, count(distinct `uc`.`id_usuario`) AS `usuarios_usando`, count(distinct `ec`.`id_evento`) AS `total_eventos`, group_concat(distinct `u`.`nombre_completo` separator ', ') AS `usuarios` FROM (((`categorias` `c` left join `usuario_categoria` `uc` on(`c`.`id_categoria` = `uc`.`id_categoria`)) left join `usuarios` `u` on(`uc`.`id_usuario` = `u`.`id_usuario`)) left join `evento_categoria` `ec` on(`c`.`id_categoria` = `ec`.`id_categoria`)) GROUP BY `c`.`id_categoria` ORDER BY `c`.`nombre` ASC ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_categorias_usuario`
--
DROP TABLE IF EXISTS `v_categorias_usuario`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_categorias_usuario`  AS SELECT `u`.`id_usuario` AS `id_usuario`, `u`.`nombre_completo` AS `usuario`, `c`.`id_categoria` AS `id_categoria`, `c`.`nombre` AS `categoria`, count(distinct `ec`.`id_evento`) AS `eventos_en_categoria` FROM (((`usuarios` `u` join `usuario_categoria` `uc` on(`u`.`id_usuario` = `uc`.`id_usuario`)) join `categorias` `c` on(`uc`.`id_categoria` = `c`.`id_categoria`)) left join `evento_categoria` `ec` on(`c`.`id_categoria` = `ec`.`id_categoria` and `ec`.`id_evento` in (select `eventos`.`id_evento` from `eventos` where `eventos`.`id_usuario` = `u`.`id_usuario`))) GROUP BY `u`.`id_usuario`, `c`.`id_categoria` ORDER BY `u`.`nombre_completo` ASC, `c`.`nombre` ASC ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_eventos`
--
DROP TABLE IF EXISTS `v_eventos`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_eventos`  AS SELECT `e`.`id_evento` AS `id_evento`, `e`.`titulo` AS `titulo`, `e`.`descripcion` AS `descripcion`, date_format(`e`.`fecha_inicio`,'%d/%m/%Y %H:%i') AS `fecha_inicio`, date_format(`e`.`fecha_fin`,'%d/%m/%Y %H:%i') AS `fecha_fin`, `e`.`ubicacion` AS `ubicacion`, CASE WHEN `e`.`recordatorio` = 1 THEN '🔔 Sí' ELSE '🔕 No' END AS `recordatorio`, `e`.`estado` AS `estado`, `u`.`nombre_completo` AS `usuario`, group_concat(`c`.`nombre` separator ', ') AS `categorias` FROM (((`eventos` `e` join `usuarios` `u` on(`e`.`id_usuario` = `u`.`id_usuario`)) left join `evento_categoria` `ec` on(`e`.`id_evento` = `ec`.`id_evento`)) left join `categorias` `c` on(`ec`.`id_categoria` = `c`.`id_categoria`)) GROUP BY `e`.`id_evento` ORDER BY `e`.`fecha_inicio` DESC ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id_categoria`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD KEY `idx_nombre` (`nombre`);

--
-- Indices de la tabla `eventos`
--
ALTER TABLE `eventos`
  ADD PRIMARY KEY (`id_evento`),
  ADD KEY `idx_usuario_evento` (`id_usuario`),
  ADD KEY `idx_fecha_inicio` (`fecha_inicio`),
  ADD KEY `idx_estado` (`estado`);

--
-- Indices de la tabla `evento_categoria`
--
ALTER TABLE `evento_categoria`
  ADD PRIMARY KEY (`id_evento`,`id_categoria`),
  ADD KEY `id_categoria` (`id_categoria`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `nombre_usuario` (`nombre_usuario`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_nombre_usuario` (`nombre_usuario`),
  ADD KEY `idx_email` (`email`);

--
-- Indices de la tabla `usuario_categoria`
--
ALTER TABLE `usuario_categoria`
  ADD PRIMARY KEY (`id_usuario`,`id_categoria`),
  ADD KEY `id_categoria` (`id_categoria`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id_categoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `eventos`
--
ALTER TABLE `eventos`
  MODIFY `id_evento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `eventos`
--
ALTER TABLE `eventos`
  ADD CONSTRAINT `eventos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE;

--
-- Filtros para la tabla `evento_categoria`
--
ALTER TABLE `evento_categoria`
  ADD CONSTRAINT `evento_categoria_ibfk_1` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id_evento`) ON DELETE CASCADE,
  ADD CONSTRAINT `evento_categoria_ibfk_2` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`) ON DELETE CASCADE;

--
-- Filtros para la tabla `usuario_categoria`
--
ALTER TABLE `usuario_categoria`
  ADD CONSTRAINT `usuario_categoria_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `usuario_categoria_ibfk_2` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
