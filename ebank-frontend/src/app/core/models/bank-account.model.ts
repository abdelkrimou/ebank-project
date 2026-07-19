export interface BankAccount {
  id: string;
  balance: number;
  createdAt: string;
  status: 'CREATED' | 'ACTIVATED' | 'SUSPENDED';
  customerDTO: { id: number; name: string; email: string };
  type: 'CurrentAccount' | 'SavingAccount';
  overDraft?: number;
  interestRate?: number;
}
