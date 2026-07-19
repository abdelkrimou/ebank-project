import { Component } from '@angular/core';
import { ChatbotService } from '../core/services/chatbot.service';

interface ChatMessage {
  role: 'user' | 'assistant';
  text: string;
}

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html'
})
export class ChatbotComponent {
  messages: ChatMessage[] = [
    { role: 'assistant', text: 'Hi! Ask me anything about your accounts, debit/credit, or how the app works.' }
  ];
  question = '';
  loading = false;

  constructor(private chatbotService: ChatbotService) {}

  send(): void {
    const text = this.question.trim();
    if (!text) return;

    this.messages.push({ role: 'user', text });
    this.question = '';
    this.loading = true;

    this.chatbotService.ask(text).subscribe({
      next: (res) => {
        this.messages.push({ role: 'assistant', text: res.answer });
        this.loading = false;
      },
      error: () => {
        this.messages.push({ role: 'assistant', text: 'Sorry, something went wrong reaching the assistant.' });
        this.loading = false;
      }
    });
  }
}
