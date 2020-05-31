-- 创建表 - 测试表
create table if not exists b_tbk_user
(
    id                   bigint not null comment '标识',
    gmt_create           datetime default CURRENT_TIMESTAMP comment '创建时间',
    gmt_modified         datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    key `idx_gmt_create` (`gmt_create`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试脚本';