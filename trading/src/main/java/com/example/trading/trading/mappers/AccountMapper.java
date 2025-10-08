package com.example.trading.trading.mappers;

import java.util.List;

import com.example.trading.trading.dto.AccountDTO;
import com.example.trading.trading.models.Account;

public class AccountMapper {
    public static AccountDTO accountToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setName(account.getName());
        dto.setBalance(account.getBalance());
        dto.setActive(account.isActive());
        return dto;
    }

    public static List<AccountDTO> accountToDTOList(List<Account> accounts) {
        return accounts.stream().map(AccountMapper::accountToDTO).toList();
    }
}
