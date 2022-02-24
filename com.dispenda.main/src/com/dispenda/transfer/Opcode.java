package com.dispenda.transfer;

public interface Opcode {
    int CLIENT_USERNAME = 1;
    int CLIENT_INVALID_USERNAME = 2;
    int CLIENT_PASSWORD = 3;
    int CLIENT_INVALID_PASSWORD = 4;
    int CLIENT_CONNECTED = 5;
}
