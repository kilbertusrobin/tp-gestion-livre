import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 10,
  duration: '30s',
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

const titles = ['Clean Code', 'Refactoring', 'The Pragmatic Programmer', 'Domain-Driven Design'];
const authors = ['Robert C. Martin', 'Martin Fowler', 'Andrew Hunt', 'Eric Evans'];

function randomItem(items) {
  return items[Math.floor(Math.random() * items.length)];
}

export default function () {
  if (Math.random() < 0.5) {
    const payload = JSON.stringify({
      title: randomItem(titles),
      author: randomItem(authors),
    });

    const response = http.post(`${BASE_URL}/books`, payload, {
      headers: { 'Content-Type': 'application/json' },
    });

    check(response, {
      'création de livre : statut 201': (r) => r.status === 201,
    });
  } else {
    const response = http.get(`${BASE_URL}/books`);

    check(response, {
      'liste des livres : statut 200': (r) => r.status === 200,
    });
  }
}
