import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BankAccount } from '../core/models/bank-account.model';
import { AccountOperation } from '../core/models/account-operation.model';
import { AccountService } from '../core/services/account.service';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html'
})
export class AccountDetailsComponent implements OnInit {
  accountId!: string;
  account: BankAccount | null = null;
  operations: AccountOperation[] = [];

  opForm: FormGroup;
  transferForm: FormGroup;

  message: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private accountService: AccountService,
    private fb: FormBuilder
  ) {
    this.opForm = this.fb.group({
      amount: [0, [Validators.required, Validators.min(0.01)]],
      description: ['']
    });
    this.transferForm = this.fb.group({
      accountDestination: ['', Validators.required],
      amount: [0, [Validators.required, Validators.min(0.01)]]
    });
  }

  ngOnInit(): void {
    this.accountId = this.route.snapshot.params['id'];
    this.load();
  }

  load(): void {
    this.accountService.getAccount(this.accountId).subscribe(a => (this.account = a));
    this.accountService.getOperations(this.accountId).subscribe(ops => (this.operations = ops));
  }

  credit(): void {
    if (this.opForm.invalid) return;
    const { amount, description } = this.opForm.value;
    this.accountService.credit(this.accountId, amount, description || 'Credit').subscribe({
      next: () => { this.message = 'Credit successful'; this.errorMessage = null; this.load(); },
      error: (err) => { this.errorMessage = err?.error?.error ?? 'Credit failed'; this.message = null; }
    });
  }

  debit(): void {
    if (this.opForm.invalid) return;
    const { amount, description } = this.opForm.value;
    this.accountService.debit(this.accountId, amount, description || 'Debit').subscribe({
      next: () => { this.message = 'Debit successful'; this.errorMessage = null; this.load(); },
      error: (err) => { this.errorMessage = err?.error?.error ?? 'Debit failed'; this.message = null; }
    });
  }

  transfer(): void {
    if (this.transferForm.invalid) return;
    const { accountDestination, amount } = this.transferForm.value;
    this.accountService.transfer(this.accountId, accountDestination, amount).subscribe({
      next: () => { this.message = 'Transfer successful'; this.errorMessage = null; this.load(); },
      error: (err) => { this.errorMessage = err?.error?.error ?? 'Transfer failed'; this.message = null; }
    });
  }
}
