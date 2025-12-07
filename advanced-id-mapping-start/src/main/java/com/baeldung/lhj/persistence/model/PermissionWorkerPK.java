package com.baeldung.lhj.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PermissionWorkerPK implements Serializable {
    @Column(name = "worker_id")
    private Long workerId;

    @Column(name = "permission_code")
    private String permissionCode;

    public PermissionWorkerPK(){}

    public PermissionWorkerPK(Long workerId, String permissionCode) {
        this.workerId = workerId;
        this.permissionCode = permissionCode;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
}
