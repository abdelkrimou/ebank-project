import { Component, OnInit } from '@angular/core';
import { ChartConfiguration, ChartData } from 'chart.js';
import { AccountService } from '../core/services/account.service';
import { CustomerService } from '../core/services/customer.service';
import { BankAccount } from '../core/models/bank-account.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  totalCustomers = 0;
  totalAccounts = 0;
  totalBalance = 0;

  accountTypeChartData: ChartData<'pie'> = { labels: [], datasets: [{ data: [] }] };
  balanceByCustomerChartData: ChartData<'bar'> = { labels: [], datasets: [{ data: [], label: 'Balance' }] };

  chartOptions: ChartConfiguration['options'] = { responsive: true };

  constructor(private accountService: AccountService, private customerService: CustomerService) {}

  ngOnInit(): void {
    this.customerService.getCustomers().subscribe(customers => (this.totalCustomers = customers.length));

    this.accountService.getAccounts().subscribe(accounts => {
      this.totalAccounts = accounts.length;
      this.totalBalance = accounts.reduce((sum, a) => sum + a.balance, 0);
      this.buildAccountTypeChart(accounts);
      this.buildBalanceByCustomerChart(accounts);
    });
  }

  private buildAccountTypeChart(accounts: BankAccount[]): void {
    const current = accounts.filter(a => a.type === 'CurrentAccount').length;
    const saving = accounts.filter(a => a.type === 'SavingAccount').length;
    this.accountTypeChartData = {
      labels: ['Current accounts', 'Saving accounts'],
      datasets: [{ data: [current, saving], backgroundColor: ['#0d6efd', '#198754'] }]
    };
  }

  private buildBalanceByCustomerChart(accounts: BankAccount[]): void {
    const byCustomer = new Map<string, number>();
    accounts.forEach(a => {
      const name = a.customerDTO?.name ?? 'Unknown';
      byCustomer.set(name, (byCustomer.get(name) ?? 0) + a.balance);
    });
    this.balanceByCustomerChartData = {
      labels: Array.from(byCustomer.keys()),
      datasets: [{ data: Array.from(byCustomer.values()), label: 'Total balance', backgroundColor: '#0d6efd' }]
    };
  }
}
