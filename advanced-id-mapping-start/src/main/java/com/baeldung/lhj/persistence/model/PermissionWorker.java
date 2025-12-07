package com.baeldung.lhj.persistence.model;

import jakarta.persistence.*;

@Entity
//@IdClass(PermissionWorkerPK.class)
public class PermissionWorker {
    // Example with @IdClass
//    @Id
//    @Column(name = "worker_id")
//    private Long workerId;
//
//    @Id
//    @Column(name = "permission_code")
//    private String permissionCode;

    @EmbeddedId
    private PermissionWorkerPK permissionWorkerPK;

    @Column(name = "enabled")
    private Boolean enabled;

    public PermissionWorkerPK getPermissionWorkerPK() {
        return permissionWorkerPK;
    }

    public void setPermissionWorkerPK(PermissionWorkerPK permissionWorkerPK) {
        this.permissionWorkerPK = permissionWorkerPK;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
