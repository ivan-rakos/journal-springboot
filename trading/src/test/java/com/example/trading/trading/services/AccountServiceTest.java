package com.example.trading.trading.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.trading.trading.TestDataFactory;
import com.example.trading.trading.dto.CreateAccountDTO;
import com.example.trading.trading.dto.UpdateAccountDTO;
import com.example.trading.trading.exceptions.BusinessRuleException;
import com.example.trading.trading.exceptions.ResourceNotFoundException;
import com.example.trading.trading.models.Account;
import com.example.trading.trading.models.Trade;
import com.example.trading.trading.repositories.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    // Create Account Tests
    @Test
    public void testCreateAccount() {
        CreateAccountDTO createAccountDTO = TestDataFactory.createValidAccountDTO();

        Account account = TestDataFactory.createValidAccount(1L, "Test Account");

        when(accountRepository.existsByName(createAccountDTO.getName())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        Account createdAccount = accountService.createAccount(createAccountDTO);
        assertNotNull(createdAccount);
        assertEquals(createAccountDTO.getName(), createdAccount.getName());
        assertEquals(createAccountDTO.getBalance(), createdAccount.getBalance());
        assertTrue(createdAccount.isActive());

        verify(accountRepository).existsByName(createAccountDTO.getName());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void testCreateAccount_DuplicateName() {
        CreateAccountDTO createAccountDTO = TestDataFactory.createValidAccountDTO();

        when(accountRepository.existsByName(createAccountDTO.getName())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> accountService.createAccount(createAccountDTO));

        verify(accountRepository).existsByName(createAccountDTO.getName());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testCreateAccount_InvalidBalance() {
        CreateAccountDTO createAccountDTO = TestDataFactory.createValidAccountDTO();
        createAccountDTO.setBalance(-100.00);

        when(accountRepository.existsByName(createAccountDTO.getName())).thenReturn(false);

        assertThrows(BusinessRuleException.class, () -> accountService.createAccount(createAccountDTO));

        verify(accountRepository).existsByName(createAccountDTO.getName());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testCreateAccount_NullBalance() {
        CreateAccountDTO createAccountDTO = TestDataFactory.createValidAccountDTO();
        createAccountDTO.setBalance(null);
        Account account = TestDataFactory.createValidAccount(1L, "Test Account");
        account.setBalance(0.0);

        when(accountRepository.existsByName(createAccountDTO.getName())).thenReturn(false);
        assertThrows(BusinessRuleException.class, () -> accountService.createAccount(createAccountDTO));

        verify(accountRepository).existsByName(createAccountDTO.getName());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testCreateAccount_Active() {
        CreateAccountDTO createAccountDTO = TestDataFactory.createValidAccountDTO();
        Account account = TestDataFactory.createValidAccount(1L, "Test Account");

        when(accountRepository.existsByName(createAccountDTO.getName())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        Account createdAccount = accountService.createAccount(createAccountDTO);
        assertNotNull(createdAccount);
        assertTrue(createdAccount.isActive());

        verify(accountRepository).existsByName(createAccountDTO.getName());
        verify(accountRepository).save(any(Account.class));
    }

    // Update Account Tests
    @Test
    public void testUpdateAccount_NonExistent() {
        Long accountId = 1L;
        UpdateAccountDTO updateAccountDTO = TestDataFactory.createValidUpdateAccountDTO();

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.updateAccount(accountId, updateAccountDTO));

        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testUpdateAccount_SameName() {
        Long accountId = 1L;
        UpdateAccountDTO updateAccountDTO = TestDataFactory.createValidUpdateAccountDTO();
        updateAccountDTO.setName("Test Account");
        Account accountToUpdate = TestDataFactory.updatedAccount();
        Account accountWithSameName = TestDataFactory.createValidAccount(2L, "Test Account");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountToUpdate));
        when(accountRepository.existsByName("Test Account")).thenReturn(true);
        when(accountRepository.findByName("Test Account")).thenReturn(accountWithSameName);

        assertThrows(BusinessRuleException.class, () -> accountService.updateAccount(accountId, updateAccountDTO));

        verify(accountRepository).findById(accountId);
        verify(accountRepository).existsByName("Test Account");
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testUpdateAccount_InvalidBalance() {
        Long accountId = 1L;
        UpdateAccountDTO updateAccountDTO = TestDataFactory.createValidUpdateAccountDTO();
        updateAccountDTO.setBalance(-500.00);
        Account existingAccount = TestDataFactory.updatedAccount();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));

        assertThrows(BusinessRuleException.class, () -> accountService.updateAccount(accountId, updateAccountDTO));

        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testUpdateAccount_JustNewData() {
        Long accountId = 1L;
        String newName = "Updated Account 2";
        UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO();
        updateAccountDTO.setName(newName);
        Account existingAccount = TestDataFactory.updatedAccount();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);

        Account updatedAccount = accountService.updateAccount(accountId, updateAccountDTO);
        assertNotNull(updatedAccount);
        assertEquals(newName, updatedAccount.getName());
        assertEquals(existingAccount.getBalance(), updatedAccount.getBalance());
        assertTrue(updatedAccount.isActive());

        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void testUpdateAccount_NoChanges() {
        Long accountId = 1L;
        UpdateAccountDTO updateAccountDTO = TestDataFactory.createValidUpdateAccountDTO();
        Account existingAccount = TestDataFactory.updatedAccount();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        assertThrows(BusinessRuleException.class, () -> accountService.updateAccount(accountId, updateAccountDTO));

        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    // GET Account Tests
    @Test
    public void testGetById_Existing() {
        Long accountId = 1L;
        Account existingAccount = TestDataFactory.createValidAccount(1L, "Test Account");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.existsById(accountId)).thenReturn(true);
        Optional<Account> account = accountService.getById(accountId);
        assertEquals(existingAccount, account.get());

        verify(accountRepository).findById(accountId);
    }

    @Test
    public void testGetById_NonExistent() {
        Long accountId = 1L;
        when(accountRepository.existsById(accountId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> accountService.getById(accountId));
        verify(accountRepository).existsById(accountId);
        verify(accountRepository, never()).findById(accountId);
    }

    @Test
    public void testGetAllAccounts() {
        List<Account> accountsMock = TestDataFactory.getAccountList();
        when(accountRepository.findAll()).thenReturn(accountsMock);
        List<Account> accounts = accountService.getAllAccounts();
        assertTrue(accounts.isEmpty() == false);
        verify(accountRepository).findAll();
    }

    @Test
    public void testGetTradesByAccountId_Existing() {
        Long accountId = 1L;
        Account existingAccount = TestDataFactory.createValidAccount(1L, "Test Account");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        List<Trade> trades = accountService.getTradesByAccountId(accountId);
        assertNotNull(trades);
        assertTrue(!trades.isEmpty());

        verify(accountRepository).findById(accountId);
    }

    @Test
    public void testGetTradesByAccountId_WithoutTrades() {
        Long accountId = 1L;
        Account existingAccount = TestDataFactory.createValidAccount(1L, "Test Account");
        existingAccount.getTrades().clear();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        List<Trade> trades = accountService.getTradesByAccountId(accountId);
        assertNotNull(trades);
        assertTrue(trades.isEmpty());

        verify(accountRepository).findById(accountId);
    }

    // Delete Account Tests
    @Test
    public void testDeleteAccount_Existing() {
        Long accountId = 1L;

        when(accountRepository.existsById(accountId)).thenReturn(true);

        accountService.deleteAccount(accountId);

        verify(accountRepository).existsById(accountId);
        verify(accountRepository).deleteById(accountId);
    }

    @Test
    public void testDeleteAccount_NonExistent() {
        Long accountId = 1L;

        when(accountRepository.existsById(accountId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccount(accountId));

        verify(accountRepository).existsById(accountId);
        verify(accountRepository, never()).deleteById(accountId);
    }

}
