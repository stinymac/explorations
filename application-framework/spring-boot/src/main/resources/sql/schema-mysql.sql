SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `sample`.`department`;
CREATE TABLE `sample`.`department` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `name` varchar(255) DEFAULT NULL COMMENT '部门名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sample`.`employee`;
CREATE TABLE `sample`.`employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `gender` smallint(2) DEFAULT NULL COMMENT '性别 0-女 1-男',
  `department_id` int(11) DEFAULT NULL COMMENT '所在部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

