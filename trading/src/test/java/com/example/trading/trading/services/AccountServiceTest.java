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

import com.example.trading.trading.dto.CreateAccountDTO;
import com.example.trading.trading.dto.UpdateAccountDTO;
import com.example.trading.trading.exceptions.BusinessRuleException;
import com.example.trading.trading.exceptions.ResourceNotFoundException;
import com.example.trading.trading.models.Account;
import com.example.trading.trading.repositories.AccountRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private CreateAccountDTO createValidAccountDTO() {
        CreateAccountDTO dto = new CreateAccountDTO();
        dto.setName("Test Account");
        dto.setBalance(1000.00);
        return dto;
    }

    private Account createValidAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setName("Test Account");
        account.setBalance(1000.00);
        account.setActive(true);
        return account;
    }

    private UpdateAccountDTO createValidUpdateAccountDTO() {
        UpdateAccountDTO dto = new UpdateAccountDTO();
        dto.setName("Updated Account");
        dto.setBalance(2000.00);
        dto.setActive(true);
        return dto;
    }

    private Account updatedAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setName("Updated Account");
        account.setBalance(2000.00);
        account.setActive(true);
        return account;
    }

    private List<Account> getAccountList() {
        return List.of(createValidAccount(), updatedAccount());
    }

    // Create Account Tests
    @Test
    public void testCreateAccount() {
        CreateAccountDTO createAccountDTO = createValidAccountDTO();

        Account account = createValidAccount();

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
        CreateAccountDTO createAccountDTO = createValidAccountDTO();

        when(accountRepository.existsByName(createAccountDTO.getName())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> accountService.createAccount(createAccountDTO));

        verify(accountRepository).existsByName(createAccountDTO.getName());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testCreateAccount_InvalidBalance() {
        CreateAccountDTO createAccountDTO = createValidAccountDTO();
        createAccountDTO.setBalance(-100.00);

        when(accountRepository.existsByName(createAccountDTO.getName())).thenReturn(false);

        assertThrows(BusinessRuleException.class, () -> accountService.createAccount(createAccountDTO));

        verify(accountRepository).existsByName(createAccountDTO.getName());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testCreateAccount_NullBalance() {
        CreateAccountDTO createAccountDTO = createValidAccountDTO();
        createAccountDTO.setBalance(null);
        Account account = createValidAccount();
        account.setBalance(0.0);

        when(accountRepository.existsByName(createAccountDTO.getName())).thenReturn(false);
        assertThrows(BusinessRuleException.class, () -> accountService.createAccount(createAccountDTO));

        verify(accountRepository).existsByName(createAccountDTO.getName());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testCreateAccount_Active() {
        CreateAccountDTO createAccountDTO = createValidAccountDTO();
        Account account = createValidAccount();

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
        UpdateAccountDTO updateAccountDTO = createValidUpdateAccountDTO();

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.updateAccount(accountId, updateAccountDTO));

        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void testUpdateAccount_SameName() {
        Long accountId = 1L;
        UpdateAccountDTO updateAccountDTO = createValidUpdateAccountDTO();
        updateAccountDTO.setName("Test Account");
        Account accountToUpdate = updatedAccount();
        Account accountWithSameName = createValidAccount();

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
        UpdateAccountDTO updateAccountDTO = createValidUpdateAccountDTO();
        updateAccountDTO.setBalance(-500.00);
        Account existingAccount = updatedAccount();

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
        Account existingAccount = updatedAccount();

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
        UpdateAccountDTO updateAccountDTO = createValidUpdateAccountDTO();
        Account existingAccount = updatedAccount();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        assertThrows(BusinessRuleException.class, () -> accountService.updateAccount(accountId, updateAccountDTO));

        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    // GET Account Tests
    @Test
    public void testGetById_Existing() {
        Long accountId = 1L;
        Account existingAccount = createValidAccount();

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
        List<Account> accountsMock = getAccountList();
        when(accountRepository.findAll()).thenReturn(accountsMock);
        List<Account> accounts = accountService.getAllAccounts();
        assertTrue(accounts.isEmpty() == false);
        verify(accountRepository).findAll();
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
