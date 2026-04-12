import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const realErrors = new Rate('real_errors');

export const options = {
  stages: [
    { duration: '10s', target: 10 },
    { duration: '10s', target: 20 },
    { duration: '20s', target: 30 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<3000'],
    real_errors: ['rate<0.05'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

function login(username, password) {
  const jar = http.cookieJar();

  const loginPage = http.get(`${BASE_URL}/login`, { jar });

  const csrfMatch = loginPage.body.match(/name="_csrf"\s+value="([^"]+)"/);
  const csrf = csrfMatch ? csrfMatch[1] : '';

  const res = http.post(
    `${BASE_URL}/login`,
    { username, password, _csrf: csrf },
    { jar }
  );

  return { jar, ok: res.status === 200 };
}

export default function () {
  const isAdmin = __VU % 2 === 0;

  const creds = isAdmin
    ? { username: 'admin', password: 'admin' }
    : { username: 'user', password: 'pass' };

  const { jar, ok } = login(creds.username, creds.password);

  realErrors.add(!ok);

  const res = http.get(`${BASE_URL}/shops`, { jar });

  check(res, {
    'status ok': (r) => r.status === 200,
  });

  realErrors.add(res.status >= 500);

  sleep(1);
}