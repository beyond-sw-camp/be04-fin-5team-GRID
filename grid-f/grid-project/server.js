import express from 'express';
import path from 'path';
import { fileURLToPath } from 'url';
import { dirname } from 'path';

const app = express();
const port = process.env.PORT || 8080;  // Elastic Beanstalk가 사용하는 포트

// __dirname을 ES 모듈에서 사용할 수 있도록 설정
const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

app.use(express.static(path.join(__dirname, 'dist')));

app.get('*', (req, res) => {
    res.sendFile(path.resolve(__dirname, 'dist', 'index.html'));
});

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});