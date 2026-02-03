const http = require('http');

const postData = JSON.stringify({
  name: 'Test Seller',
  email: 'seller_' + Date.now() + '@example.com',
  password: 'password123',
  role: 'SELLER',
  phone: '1234567890',
  companyName: 'Test Company',
  address: '123 Test Street'
});

const options = {
  hostname: 'localhost',
  port: 8081,
  path: '/api/auth/signup',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': Buffer.byteLength(postData)
  }
};

const req = http.request(options, (res) => {
  console.log(`STATUS: ${res.statusCode}`);
  console.log(`HEADERS: ${JSON.stringify(res.headers)}`);
  res.setEncoding('utf8');
  res.on('data', (chunk) => {
    console.log(`BODY: ${chunk}`);
  });
});

req.on('error', (e) => {
  console.error(`problem with request: ${e.message}`);
  console.log('Ensure the backend server is running on port 8081.');
});

// Write data to request body
req.write(postData);
req.end();
