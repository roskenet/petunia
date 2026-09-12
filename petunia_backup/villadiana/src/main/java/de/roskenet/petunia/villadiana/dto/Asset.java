package de.roskenet.petunia.villadiana.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class Asset {
    private UUID id;
    @JsonProperty("account_id")
    private UUID accountId;
    private String symbol;
    private long quantity;

    public Asset() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return quantity == asset.quantity && Objects.equals(id, asset.id) && Objects.equals(accountId, asset.accountId) && Objects.equals(symbol, asset.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, symbol, quantity);
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", symbol='" + symbol + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}