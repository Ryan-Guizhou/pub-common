package com.peach.common.entity;

import com.peach.common.generator.MapperGenerator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.beanutils.PropertyUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Map;

/**
 * @Author Mr Shu
 * @Version 1.0.0
 * @Description //TODO
 * @CreateTime 2025/05/03 15:52
 */
@Data
@Table(name = "LOGIN_LOG")
public class LoginLogDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @ApiModelProperty(value = "主键")
    private String id;

    @Column(name = "CREATOR_CODE")
    @ApiModelProperty(value = "创建人编码")
    private String creatorCode;

    @Column(name = "CREATOR")
    @ApiModelProperty(value = "创建人名称")
    private String creator;

    @Column(name = "CREATE_TIME")
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @Column(name = "PRIVATE_IP")
    @ApiModelProperty(value = "私网IP")
    private String privateIp;

    @Column(name = "PUBLIC_IP")
    @ApiModelProperty(value = "公网IP")
    private String publicIp;

    @Column(name = "DEVICE")
    @ApiModelProperty(value = "设备信息")
    private String device;

    @Column(name = "BROWSER")
    @ApiModelProperty(value = "浏览器信息")
    private String browser;

    @Column(name = "OS")
    @ApiModelProperty(value = "操作系统")
    private String os;

    @Column(name = "EXECUTION_TIME")
    @ApiModelProperty(value = "执行时间（毫秒）")
    private Long executionTime;

    @Column(name = "IS_SUCCESS")
    @ApiModelProperty(value = "是否成功 (true: 成功, false: 失败)")
    private String isSuccess;

    @Column(name = "ERROR_MSG")
    @ApiModelProperty(value = "错误信息")
    private String errorMsg;

    @Column(name = "RESPONSE_DATA")
    @ApiModelProperty(value = "响应数据")
    private String responseData;

    /**
     * 转换为Map
     *
     * @return
     */
    public Map toMap() {
        try {
            Map map = PropertyUtils.describe(this);
            map.remove("class");
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        System.out.println(MapperGenerator.genMapper(LoginLogDO.class));
    }
}
