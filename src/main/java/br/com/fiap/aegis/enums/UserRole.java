package br.com.fiap.aegis.enums;

public enum UserRole {
    ADMIN("admin"),
    ENGENHEIRO("engenheiro");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
