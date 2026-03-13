'use client'

import { useUser } from "@/context/UserContext";
import { useEffect, useState, useCallback } from "react";
import { requestJson } from "@/lib/api";
import { Card, Col, Row, Statistic, Table, Typography, Spin, Alert, Divider, Button, Modal, Form, Input, InputNumber, Radio, App } from "antd";
import { WalletOutlined, LineChartOutlined, UserOutlined, PlusOutlined } from "@ant-design/icons";

const { Title, Text } = Typography;

interface PlayerAccount {
    id: string;
    playerName: string;
    balance: number;
}

interface Asset {
    id: string;
    accountId: string;
    symbol: string;
    quantity: number;
}

enum OrderSide {
    BUY = 'BUY',
    SELL = 'SELL'
}

enum OrderType {
    MARKET = 'MARKET',
    LIMIT = 'LIMIT'
}

interface PlaceOrderRequest {
    playerName: string;
    symbol: string;
    quantity: number;
    price: number;
    side: OrderSide;
    type: OrderType;
}

export default function Dashboard() {
    const { user, isLoading: isUserLoading } = useUser();
    const [account, setAccount] = useState<PlayerAccount | null>(null);
    const [assets, setAssets] = useState<Asset[]>([]);
    const [isLoadingData, setIsLoadingData] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [isBuyModalVisible, setIsBuyModalVisible] = useState(false);
    const [isSubmittingOrder, setIsSubmittingOrder] = useState(false);
    const { message } = App.useApp();
    const [form] = Form.useForm();

    const fetchDashboardData = useCallback(async () => {
        if (!user?.sub) return;

        setIsLoadingData(true);
        setError(null);
        try {
            const accountData = await requestJson<PlayerAccount>(`/api/me/account/${user.sub}`);
            setAccount(accountData);

            const assetsData = await requestJson<Asset[]>(`/api/me/account/${user.sub}/assets`);
            setAssets(assetsData);
        } catch (err: unknown) {
            const errorMessage = err instanceof Error ? err.message : "Failed to load account details";
            console.error("Failed to fetch dashboard data:", err);
            setError(errorMessage);
        } finally {
            setIsLoadingData(false);
        }
    }, [user]);

    useEffect(() => {
        fetchDashboardData();
    }, [fetchDashboardData]);

    const handleBuy = () => {
        form.resetFields();
        form.setFieldsValue({ type: OrderType.MARKET, side: OrderSide.BUY });
        setIsBuyModalVisible(true);
    };

    const handleOrderSubmit = async (values: { symbol: string; quantity: number; type: OrderType; price?: number }) => {
        if (!user) return;

        setIsSubmittingOrder(true);
        try {
            const orderRequest: PlaceOrderRequest = {
                playerName: user.name,
                symbol: values.symbol.toUpperCase(),
                quantity: values.quantity,
                price: values.type === OrderType.LIMIT && values.price ? Math.round(values.price * 100) : 0,
                side: OrderSide.BUY,
                type: values.type,
            };

            await requestJson('/api/me/orders', {
                method: 'POST',
                body: JSON.stringify(orderRequest),
            });

            message.success(`Order placed successfully for ${values.symbol}`);
            setIsBuyModalVisible(false);
            // Refresh dashboard data to see updated balance (if it was a market order that filled immediately)
            // or just to keep state consistent.
            await fetchDashboardData();
        } catch (err: unknown) {
            const errorMessage = err instanceof Error ? err.message : "Failed to place order";
            console.error("Failed to place order:", err);
            message.error(errorMessage);
        } finally {
            setIsSubmittingOrder(false);
        }
    };

    if (isUserLoading || isLoadingData) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' }}>
                <Spin size="large" tip="Loading your portfolio..." />
            </div>
        );
    }

    if (error) {
        return (
            <div style={{ padding: '40px' }}>
                <Alert message="Error" description={error} type="error" showIcon />
            </div>
        );
    }

    if (!user) {
        return (
            <div style={{ padding: '40px' }}>
                <Alert message="Not Authenticated" description="Please log in to view your dashboard." type="warning" showIcon />
            </div>
        );
    }

    const assetColumns = [
        {
            title: 'Symbol',
            dataIndex: 'symbol',
            key: 'symbol',
            render: (text: string) => <Text strong>{text}</Text>,
        },
        {
            title: 'Quantity',
            dataIndex: 'quantity',
            key: 'quantity',
            align: 'right' as const,
            render: (val: number) => val.toLocaleString(),
        },
        {
            title: 'Action',
            key: 'action',
            align: 'center' as const,
            render: (_: unknown, record: Asset) => (
                <Button 
                    type="primary" 
                    size="small" 
                    onClick={() => {
                        form.setFieldsValue({ symbol: record.symbol, type: OrderType.MARKET });
                        setIsBuyModalVisible(true);
                    }}
                >
                    Buy More
                </Button>
            ),
        },
    ];

    return (
        <div style={{ padding: '24px', maxWidth: '1200px', margin: '0 auto' }}>
            <Row gutter={[16, 16]}>
                <Col span={24}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <div>
                            <Title level={2} style={{ margin: 0 }}>
                                <UserOutlined /> Dashboard
                            </Title>
                            <Text type="secondary">Welcome back, {user.name}</Text>
                        </div>
                        <Button type="primary" icon={<PlusOutlined />} size="large" onClick={handleBuy}>
                            New Buy Order
                        </Button>
                    </div>
                    <Divider />
                </Col>

                <Col xs={24} sm={12}>
                    <Card bordered={false} className="shadow-sm">
                        <Statistic
                            title="Account Balance"
                            value={account ? account.balance / 100 : 0}
                            precision={2}
                            prefix={<WalletOutlined />}
                            suffix="€"
                        />
                        <Text type="secondary" style={{ fontSize: '12px' }}>
                            Account ID: {account?.id || 'N/A'}
                        </Text>
                    </Card>
                </Col>

                <Col xs={24} sm={12}>
                    <Card bordered={false} className="shadow-sm">
                        <Statistic
                            title="Total Assets"
                            value={assets.length}
                            prefix={<LineChartOutlined />}
                        />
                        <Text type="secondary" style={{ fontSize: '12px' }}>
                           Positions in your portfolio
                        </Text>
                    </Card>
                </Col>

                <Col span={24}>
                    <Card title="Portfolio" bordered={false} className="shadow-sm" style={{ marginTop: '16px' }}>
                        <Table
                            dataSource={assets}
                            columns={assetColumns}
                            rowKey="id"
                            pagination={false}
                            locale={{ emptyText: 'No assets found in your portfolio' }}
                        />
                    </Card>
                </Col>
            </Row>

            <Modal
                title="Place Buy Order"
                open={isBuyModalVisible}
                onOk={() => form.submit()}
                onCancel={() => setIsBuyModalVisible(false)}
                confirmLoading={isSubmittingOrder}
                okText="Place Order"
                destroyOnClose
            >
                <Form
                    form={form}
                    layout="vertical"
                    onFinish={handleOrderSubmit}
                    initialValues={{ type: OrderType.MARKET }}
                >
                    <Form.Item
                        name="symbol"
                        label="Stock Symbol"
                        rules={[
                            { required: true, message: 'Please enter a stock symbol' },
                            { len: 3, message: 'Symbol must be exactly 3 letters' }
                        ]}
                    >
                        <Input placeholder="e.g. BAY" maxLength={3} style={{ textTransform: 'uppercase' }} />
                    </Form.Item>

                    <Form.Item
                        name="quantity"
                        label="Amount of Shares"
                        rules={[{ required: true, message: 'Please enter the amount of shares' }]}
                    >
                        <InputNumber min={1} style={{ width: '100%' }} />
                    </Form.Item>

                    <Form.Item
                        name="type"
                        label="Order Type"
                        rules={[{ required: true }]}
                    >
                        <Radio.Group>
                            <Radio value={OrderType.MARKET}>Market</Radio>
                            <Radio value={OrderType.LIMIT}>Limit</Radio>
                        </Radio.Group>
                    </Form.Item>

                    <Form.Item
                        noStyle
                        shouldUpdate={(prevValues, currentValues) => prevValues.type !== currentValues.type}
                    >
                        {({ getFieldValue }) =>
                            getFieldValue('type') === OrderType.LIMIT ? (
                                <Form.Item
                                    name="price"
                                    label="Limit Price (€)"
                                    rules={[{ required: true, message: 'Please enter the limit price' }]}
                                >
                                    <InputNumber
                                        min={0.01}
                                        step={0.01}
                                        precision={2}
                                        style={{ width: '100%' }}
                                        formatter={(value) => `${value} €`}
                                        parser={(value) => value!.replace(' €', '')}
                                    />
                                </Form.Item>
                            ) : null
                        }
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
}