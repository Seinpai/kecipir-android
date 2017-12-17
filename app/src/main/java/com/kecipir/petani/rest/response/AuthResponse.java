package com.kecipir.petani.rest.response;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

public class AuthResponse {
    private Credential data;

    public Credential getCredential() {
        return data;
    }

    public static class Credential {
        private String token;
        private String name;
        private String email;
        private String photo;

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getPhoto() {
            return photo;
        }

        public String getToken() {
            return token;
        }
    }
}
