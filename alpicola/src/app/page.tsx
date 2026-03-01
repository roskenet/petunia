"use client";

import { Layout, Typography, Button, Space, Card, Divider } from 'antd';
import Image from 'next/image';
import { ArrowRightOutlined, SmileOutlined, WarningOutlined } from '@ant-design/icons';

const { Content, Footer } = Layout;
const { Title, Paragraph, Text } = Typography;

export default function Home() {
  return (
    <Layout style={{ minHeight: '100vh', background: '#f0f2f5' }}>
      <Content style={{ padding: '0 50px', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
        <div style={{ maxWidth: '800px', width: '100%', padding: '40px 0' }}>
          <Card 
            style={{ 
              borderRadius: '16px', 
              boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
              textAlign: 'center'
            }}
          >
            <Space direction="vertical" size="large" style={{ width: '100%' }}>
              <div style={{ margin: '20px 0' }}>
                <Image 
                  src="/img/petunia.png" 
                  width={150} 
                  height={150} 
                  alt="Petunia Logo" 
                  style={{ borderRadius: '50%', border: '4px solid #1890ff' }}
                />
              </div>

              <Title level={1} style={{ margin: 0 }}>
                Welcome to... something? <SmileOutlined />
              </Title>

              <Divider plain><Text type="secondary">The Sincerity Section</Text></Divider>

              <div style={{ padding: '0 40px' }}>
                <Title level={3} type="danger">
                  <WarningOutlined /> Spoiler Alert: You&apos;re wasting your time.
                </Title>
                <Paragraph style={{ fontSize: '18px', lineHeight: '1.6' }}>
                  If you were looking for groundbreaking insights, life-changing content, or even a basic 
                  reason to stay on this page, I have some unfortunate news. There is <Text strong underline>absolutely nothing</Text> useful here. 
                  Zero. Nada. Zilch.
                </Paragraph>
                <Paragraph italic style={{ color: '#8c8c8c' }}>
                  &quot;I&apos;ve seen empty boxes with more substance than this website.&quot; — A very honest reviewer.
                </Paragraph>
              </div>

              <Divider />

              <Paragraph style={{ fontSize: '16px' }}>
                Still here? Your curiosity is either impressive or deeply concerning. 
                What could possibly be hidden behind the next click? Is it more of nothing, or 
                the secret to the universe? (It&apos;s probably just more of nothing, but you won&apos;t know unless you try).
              </Paragraph>

              <Button 
                type="primary" 
                size="large" 
                icon={<ArrowRightOutlined />} 
                href="/petunias"
                style={{ height: '50px', padding: '0 40px', fontSize: '18px', borderRadius: '25px' }}
              >
                Go to Petunias
              </Button>

              <Footer style={{ textAlign: 'center', background: 'transparent', color: '#bfbfbf' }}>
                Felix Roske ©2026 — Dedicated to the art of providing no value.
              </Footer>
            </Space>
          </Card>
        </div>
      </Content>
    </Layout>
  );
}
