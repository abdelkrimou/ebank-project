import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Customer } from '../core/models/customer.model';
import { CustomerService } from '../core/services/customer.service';

@Component({
  selector: 'app-customers-list',
  templateUrl: './customers-list.component.html'
})
export class CustomersListComponent implements OnInit {
  customers: Customer[] = [];
  keyword = '';
  errorMessage: string | null = null;

  constructor(private customerService: CustomerService, private router: Router) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.customerService.getCustomers().subscribe({
      next: (data) => (this.customers = data),
      error: () => (this.errorMessage = 'Could not load customers')
    });
  }

  search(): void {
    if (!this.keyword.trim()) {
      this.loadCustomers();
      return;
    }
    this.customerService.searchCustomers(this.keyword).subscribe({
      next: (data) => (this.customers = data),
      error: () => (this.errorMessage = 'Search failed')
    });
  }

  deleteCustomer(id: number | undefined): void {
    if (id == null) return;
    if (!confirm('Delete this customer?')) return;
    this.customerService.deleteCustomer(id).subscribe({
      next: () => this.loadCustomers(),
      error: () => (this.errorMessage = 'Delete failed')
    });
  }

  viewAccounts(customerId: number | undefined): void {
    if (customerId == null) return;
    this.router.navigate(['/accounts'], { queryParams: { customerId } });
  }
}
