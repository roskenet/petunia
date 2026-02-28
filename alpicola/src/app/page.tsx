"use client";

import { Typography, Layout, Space, Button } from 'antd';
import Image from 'next/image';

const { Title, Paragraph } = Typography;
const { Content } = Layout;

export default function Home() {
  return (
    <Layout style={{ minHeight: '100vh', background: 'var(--background)' }}>
      <Content style={{ 
        padding: '50px', 
        display: 'flex', 
        flexDirection: 'column', 
        justifyContent: 'center', 
        alignItems: 'center' 
      }}>
        <Space direction="vertical" align="center">
          <Image src="/img/chocolina.jpg" width={200} height={200} alt="Chocolina" />
          <Title level={1}>Hello World!</Title>
          <Paragraph>This is my webservice garden!</Paragraph>
          <Title level={4}>It uses the following Technologies, Products, Languages, Tools:</Title>

          <Paragraph>Ant Design, Docker, Gradle, Grafana, Jaeger, Java,
            Keycloak, Kotlin, Kubernetes, Make, Maven, Microservices,
            Nakadi, Next.js, PostgreSQL, Prometheus, Spring, SpringBoot, OpenID Connect, Valkey, Websockets</Paragraph>

          <Button type="primary" href="/petunias">My Petunias</Button>
        </Space>
      </Content>
    </Layout>
  );
}
