# E-Bank — Angular Client (Part 2 + Part 5)

Angular (NgModule-based, v17) front-end for the E-Bank Spring Boot backend.

## What's here

- JWT login (`/login`), token stored in `localStorage`, attached to every
  request by `JwtInterceptor`
- Customers: list, search, create, delete, jump to a customer's accounts
- Accounts: list (optionally filtered by customer), account detail page with
  credit / debit / transfer forms and operation history
- Dashboard: totals + two Chart.js charts (account type split, balance by
  customer) via `ng2-charts`
- Chatbot: a chat widget at `/chatbot` talking to the backend's
  `POST /chatbot/ask` RAG endpoint (Part 5 — same backend service the
  Telegram bot uses)
- Route guard (`AuthGuard`) protecting everything except `/login`

## Running it

```bash
npm install
ng serve
```

Then open `http://localhost:4200`. The backend is expected at
`http://localhost:8085` (see `src/environments/environment.ts`) — start
the Spring Boot app first. Dev login: `admin` / `admin123` (seeded by the
backend — change it once you've confirmed login works).

## Not yet done / known gaps

- No user registration UI (backend has `POST /auth/register`, just no form
  for it yet) and no "change password" form (backend endpoint exists)
- No customer/account edit forms, only create/delete
- No route-level role checks (e.g. admin-only pages) — the backend has
  roles (`ROLE_USER` / `ROLE_ADMIN`) but the frontend doesn't branch on them
  yet
- No pagination on the customers/accounts tables
- Styling is plain Bootstrap, not customized

## A note on the OpenAI key

This app never talks to OpenAI directly — it only calls the Spring Boot
backend, which holds the key server-side via an environment variable. Don't
put an OpenAI key anywhere in this frontend project.
