"use client";

import { Typography, Layout, Space } from 'antd';

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
          <Title level={1}>Hello World!</Title>
          <Paragraph>This is my webservice garden!</Paragraph>
        </Space>
      </Content>
    </Layout>
  );
}
