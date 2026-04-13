import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '20s', target: 5 },   // плавний старт
    { duration: '30s', target: 15 },  // середнє навантаження
    { duration: '30s', target: 30 },  // стрес
    { duration: '20s', target: 0 },   // спад
  ],

  thresholds: {
    http_req_duration: ['p(95)<3000'], // 95% запитів < 3 сек
    http_req_failed: ['rate<0.2'],     // допускаємо до 20% помилок
  },
};

const BASE_URL = 'http://localhost:8080';

export default function () {
  // Головна сторінка
  let res = http.get(`${BASE_URL}/`);
  check(res, {
    'home page status 200': (r) => r.status === 200,
  });

  sleep(1);

  // Список магазинів
  res = http.get(`${BASE_URL}/shops`);
  check(res, {
    'shops page status 200': (r) => r.status === 200,
  });

  sleep(1);

  // Сторінка логіну
  res = http.get(`${BASE_URL}/login`);
  check(res, {
    'login page loads': (r) => r.status === 200,
  });

  sleep(1);
}