import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BankAccount } from '../core/models/bank-account.model';
import { AccountService } from '../core/services/account.service';

@Component({
  selector: 'app-accounts-list',
  templateUrl: './accounts-list.component.html'
})
export class AccountsListComponent implements OnInit {
  accounts: BankAccount[] = [];
  customerIdFilter: number | null = null;
  errorMessage: string | null = null;

  constructor(private accountService: AccountService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.customerIdFilter = params['customerId'] ? +params['customerId'] : null;
      this.loadAccounts();
    });
  }

  loadAccounts(): void {
    this.accountService.getAccounts().subscribe({
      next: (data) => {
        this.accounts = this.customerIdFilter
          ? data.filter(a => a.customerDTO?.id === this.customerIdFilter)
          : data;
      },
      error: () => (this.errorMessage = 'Could not load accounts')
    });
  }

  openAccount(id: string): void {
    this.router.navigate(['/accounts', id]);
  }
}
