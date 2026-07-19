import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CustomerService } from '../core/services/customer.service';

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html'
})
export class NewCustomerComponent {
  form: FormGroup;
  errorMessage: string | null = null;

  constructor(private fb: FormBuilder, private customerService: CustomerService, private router: Router) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.customerService.saveCustomer(this.form.value).subscribe({
      next: () => this.router.navigate(['/customers']),
      error: () => (this.errorMessage = 'Could not create customer')
    });
  }
}
