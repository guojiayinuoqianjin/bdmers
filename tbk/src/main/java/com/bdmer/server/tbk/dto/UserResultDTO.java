package com.bdmer.server.tbk.dto;

import com.bdmer.server.tbk.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户 - ResultDTO
 *
 * @author GongDeLang
 * @since  2020年06月01日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserResultDTO extends UserEntity {
}