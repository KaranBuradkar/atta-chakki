package com.atachakki.exception.entityNotFound;

public class StaffPermissionNotFoundException extends EntityNotFoundException{
    public StaffPermissionNotFoundException(Long permissionId) {
        super("staff permission not found id "+permissionId, null);
    }
}
