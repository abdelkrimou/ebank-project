import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { BankAccount } from '../models/bank-account.model';
import { AccountOperation } from '../models/account-operation.model';

@Injectable({ providedIn: 'root' })
export class AccountService {

  constructor(private http: HttpClient) {}

  getAccounts(): Observable<BankAccount[]> {
    return this.http.get<BankAccount[]>(`${environment.apiUrl}/accounts`);
  }

  getAccount(id: string): Observable<BankAccount> {
    return this.http.get<BankAccount>(`${environment.apiUrl}/accounts/${id}`);
  }

  getOperations(accountId: string): Observable<AccountOperation[]> {
    return this.http.get<AccountOperation[]>(`${environment.apiUrl}/accounts/${accountId}/operations`);
  }

  createCurrentAccount(customerId: number, initialBalance: number, overDraft: number): Observable<BankAccount> {
    return this.http.post<BankAccount>(
      `${environment.apiUrl}/accounts/current/${customerId}`, null,
      { params: { initialBalance, overDraft } }
    );
  }

  createSavingAccount(customerId: number, initialBalance: number, interestRate: number): Observable<BankAccount> {
    return this.http.post<BankAccount>(
      `${environment.apiUrl}/accounts/saving/${customerId}`, null,
      { params: { initialBalance, interestRate } }
    );
  }

  debit(accountId: string, amount: number, description: string): Observable<any> {
    return this.http.post(`${environment.apiUrl}/accounts/debit`, { accountId, amount, description });
  }

  credit(accountId: string, amount: number, description: string): Observable<any> {
    return this.http.post(`${environment.apiUrl}/accounts/credit`, { accountId, amount, description });
  }

  transfer(accountSource: string, accountDestination: string, amount: number): Observable<any> {
    return this.http.post(`${environment.apiUrl}/accounts/transfer`, { accountSource, accountDestination, amount });
  }
}
