<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${basePackageDao}.${entityName}Mapper">

    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        ${insert}
    </insert>

    <delete id="delete">
        ${delete}
    </delete>

    <update id="update">
        ${update}
    </update>

    <select id="select" resultType="${basePackageDao}.${entityName}">
        ${select}
    </select>


</mapper>