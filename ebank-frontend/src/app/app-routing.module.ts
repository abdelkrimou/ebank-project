import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { CustomersListComponent } from './customers/customers-list.component';
import { NewCustomerComponent } from './customers/new-customer.component';
import { AccountsListComponent } from './accounts/accounts-list.component';
import { AccountDetailsComponent } from './accounts/account-details.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ChatbotComponent } from './chatbot/chatbot.component';
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'customers', component: CustomersListComponent, canActivate: [AuthGuard] },
  { path: 'customers/new', component: NewCustomerComponent, canActivate: [AuthGuard] },
  { path: 'accounts', component: AccountsListComponent, canActivate: [AuthGuard] },
  { path: 'accounts/:id', component: AccountDetailsComponent, canActivate: [AuthGuard] },
  { path: 'chatbot', component: ChatbotComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
