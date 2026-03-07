"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import {
  Button,
  Card,
  Form,
  Input,
  InputNumber,
  Layout,
  Modal,
  Popconfirm,
  Space,
  Table,
  Tabs,
  Typography,
  message,
} from "antd";
import type { ColumnsType } from "antd/es/table";
import { requestJson } from "@/lib/api";

const { Content } = Layout;
const { Title, Paragraph, Text } = Typography;

type PlayerDto = {
  id: string;
  player_name: string;
  balance: number;
};

type PlayerFormValues = {
  player_name: string;
  balance: number;
};

type CreatePlayerPayload = {
  player_name: string;
  initial_balance: number;
};

type SecurityDto = {
  symbol: string;
  name: string;
};

type SecurityFormValues = {
  symbol: string;
  name: string;
};

type CreateSecurityPayload = {
  symbol: string;
  name: string;
};

type UpdateSecurityPayload = {
  name: string;
};


function PlayersTab() {
  const [form] = Form.useForm<PlayerFormValues>();
  const [players, setPlayers] = useState<PlayerDto[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [playerNameFilter, setPlayerNameFilter] = useState("");

  const filteredPlayers = useMemo(() => {
    return players.filter((player) => {
      return player.player_name
        .toLowerCase()
        .includes(playerNameFilter.trim().toLowerCase());
    });
  }, [players, playerNameFilter]);

  const loadPlayers = useCallback(async () => {
    setIsLoading(true);
    try {
      const response = await requestJson<PlayerDto[]>("/api/admin/players");
      setPlayers(response ?? []);
    } catch (error) {
      message.error((error as Error).message || "Could not load players");
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    void loadPlayers();
  }, [loadPlayers]);

  const columns: ColumnsType<PlayerDto> = useMemo(
    () => [
      {
        title: "Player",
        dataIndex: "player_name",
        key: "player_name",
      },
      {
        title: "Balance",
        dataIndex: "balance",
        key: "balance",
      },
      {
        title: "Actions",
        key: "actions",
        render: (_, record) => (
          <Popconfirm
            title={`Delete ${record.player_name}?`}
            description="This cannot be undone."
            okText="Delete"
            cancelText="Cancel"
            onConfirm={() => void handleDelete(record.player_name)}
          >
            <Button danger>Delete</Button>
          </Popconfirm>
        ),
      },
    ],
    []
  );

  const openCreateModal = () => {
    form.setFieldsValue({ player_name: "", balance: 0 });
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    form.resetFields();
  };

  const handleSave = async () => {
    const values = await form.validateFields();
    setIsSaving(true);

    try {
      const payload: CreatePlayerPayload = {
        player_name: values.player_name,
        initial_balance: values.balance,
      };
      await requestJson<PlayerDto>("/api/admin/players", {
        method: "POST",
        body: JSON.stringify(payload),
      });
      message.success("Player created");

      closeModal();
      await loadPlayers();
    } catch (error) {
      message.error((error as Error).message || "Could not save player");
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete = async (playerName: string) => {
    try {
      await requestJson<void>(`/api/admin/players/${encodeURIComponent(playerName)}`, {
        method: "DELETE",
      });
      message.success("Player deleted");
      await loadPlayers();
    } catch (error) {
      message.error((error as Error).message || "Could not delete player");
    }
  };

  return (
    <Space direction="vertical" size="middle" style={{ width: "100%" }}>
      <Space style={{ width: "100%", justifyContent: "space-between" }}>
        <div>
          <Title level={4} style={{ margin: 0 }}>
            Players
          </Title>
          <Text type="secondary">Create and delete player accounts.</Text>
        </div>
        <Button type="primary" onClick={openCreateModal}>
          New Player
        </Button>
      </Space>

      <Space wrap style={{ width: "100%" }}>
        <Input
          allowClear
          placeholder="Search player name"
          value={playerNameFilter}
          onChange={(event) => setPlayerNameFilter(event.target.value)}
          style={{ width: 240 }}
        />
        <Button onClick={() => {
          setPlayerNameFilter("");
        }}>
          Reset Filters
        </Button>
        <Text type="secondary">
          {filteredPlayers.length}/{players.length} shown
        </Text>
      </Space>

      <Table<PlayerDto>
        rowKey="id"
        columns={columns}
        dataSource={filteredPlayers}
        loading={isLoading}
        pagination={false}
      />

      <Modal
        title="Create Player"
        open={isModalOpen}
        confirmLoading={isSaving}
        onOk={() => void handleSave()}
        onCancel={closeModal}
        okText="Create"
      >
        <Form<PlayerFormValues> form={form} layout="vertical">
          <Form.Item
            label="Player Name"
            name="player_name"
            rules={[{ required: true, message: "Please enter a player name" }]}
          >
            <Input placeholder="e.g. alice" />
          </Form.Item>

          <Form.Item
            label="Balance"
            name="balance"
            rules={[{ required: true, message: "Please enter a balance" }]}
          >
            <InputNumber style={{ width: "100%" }} min={0} />
          </Form.Item>
        </Form>
      </Modal>
    </Space>
  );
}

function SecuritiesTab() {
  const [form] = Form.useForm<SecurityFormValues>();
  const [securities, setSecurities] = useState<SecurityDto[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingSecurity, setEditingSecurity] = useState<SecurityDto | null>(null);
  const [isSaving, setIsSaving] = useState(false);
  const [symbolFilter, setSymbolFilter] = useState("");

  const filteredSecurities = useMemo(() => {
    return securities.filter((security) => {
      return security.symbol
        .toLowerCase()
        .includes(symbolFilter.trim().toLowerCase()) ||
        security.name
          .toLowerCase()
          .includes(symbolFilter.trim().toLowerCase());
    });
  }, [securities, symbolFilter]);

  const loadSecurities = useCallback(async () => {
    setIsLoading(true);
    try {
      const response = await requestJson<SecurityDto[]>("/api/admin/securities");
      setSecurities(response ?? []);
    } catch (error) {
      message.error((error as Error).message || "Could not load securities");
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    void loadSecurities();
  }, [loadSecurities]);

  const columns: ColumnsType<SecurityDto> = useMemo(
    () => [
      {
        title: "Symbol",
        dataIndex: "symbol",
        key: "symbol",
      },
      {
        title: "Name",
        dataIndex: "name",
        key: "name",
      },
      {
        title: "Actions",
        key: "actions",
        render: (_, record) => (
          <Space>
            <Button onClick={() => openEditModal(record)}>Edit</Button>
            <Popconfirm
              title={`Delete ${record.symbol}?`}
              description="This cannot be undone."
              okText="Delete"
              cancelText="Cancel"
              onConfirm={() => void handleDelete(record.symbol)}
            >
              <Button danger>Delete</Button>
            </Popconfirm>
          </Space>
        ),
      },
    ],
    []
  );

  const openCreateModal = () => {
    setEditingSecurity(null);
    form.setFieldsValue({ symbol: "", name: "" });
    setIsModalOpen(true);
  };

  const openEditModal = (security: SecurityDto) => {
    setEditingSecurity(security);
    form.setFieldsValue({
      symbol: security.symbol,
      name: security.name,
    });
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setEditingSecurity(null);
    form.resetFields();
  };

  const handleSave = async () => {
    const values = await form.validateFields();
    setIsSaving(true);

    try {
      if (editingSecurity) {
        const payload: UpdateSecurityPayload = {
          name: values.name,
        };
        await requestJson<SecurityDto>(`/api/admin/securities/${encodeURIComponent(editingSecurity.symbol)}`, {
          method: "PUT",
          body: JSON.stringify(payload),
        });
        message.success("Security updated");
      } else {
        const payload: CreateSecurityPayload = {
          symbol: values.symbol,
          name: values.name,
        };
        await requestJson<SecurityDto>("/api/admin/securities", {
          method: "POST",
          body: JSON.stringify(payload),
        });
        message.success("Security created");
      }

      closeModal();
      await loadSecurities();
    } catch (error) {
      message.error((error as Error).message || "Could not save security");
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete = async (symbol: string) => {
    try {
      await requestJson<void>(`/api/admin/securities/${encodeURIComponent(symbol)}`, {
        method: "DELETE",
      });
      message.success("Security deleted");
      await loadSecurities();
    } catch (error) {
      message.error((error as Error).message || "Could not delete security");
    }
  };

  return (
    <Space direction="vertical" size="middle" style={{ width: "100%" }}>
      <Space style={{ width: "100%", justifyContent: "space-between" }}>
        <div>
          <Title level={4} style={{ margin: 0 }}>
            Securities
          </Title>
          <Text type="secondary">Create, edit and delete securities.</Text>
        </div>
        <Button type="primary" onClick={openCreateModal}>
          New Security
        </Button>
      </Space>

      <Space wrap style={{ width: "100%" }}>
        <Input
          allowClear
          placeholder="Search symbol or name"
          value={symbolFilter}
          onChange={(event) => setSymbolFilter(event.target.value)}
          style={{ width: 240 }}
        />
        <Button onClick={() => {
          setSymbolFilter("");
        }}>
          Reset Filters
        </Button>
        <Text type="secondary">
          {filteredSecurities.length}/{securities.length} shown
        </Text>
      </Space>

      <Table<SecurityDto>
        rowKey="symbol"
        columns={columns}
        dataSource={filteredSecurities}
        loading={isLoading}
        pagination={false}
      />

      <Modal
        title={editingSecurity ? "Edit Security" : "Create Security"}
        open={isModalOpen}
        confirmLoading={isSaving}
        onOk={() => void handleSave()}
        onCancel={closeModal}
        okText={editingSecurity ? "Save" : "Create"}
      >
        <Form<SecurityFormValues> form={form} layout="vertical">
          <Form.Item
            label="Symbol"
            name="symbol"
            rules={[{ required: true, message: "Please enter a symbol" }]}
          >
            <Input
              placeholder="e.g. AAPL"
              disabled={!!editingSecurity}
              style={{ textTransform: "uppercase" }}
            />
          </Form.Item>

          <Form.Item
            label="Name"
            name="name"
            rules={[{ required: true, message: "Please enter a name" }]}
          >
            <Input placeholder="e.g. Apple Inc." />
          </Form.Item>
        </Form>
      </Modal>
    </Space>
  );
}

const tabItems = [
  {
    key: "players",
    label: "Players",
    children: <PlayersTab />,
  },
  {
    key: "securities",
    label: "Securities",
    children: <SecuritiesTab />,
  },
  {
    key: "orders",
    label: "Orders",
    children: (
      <>
        <Title level={4}>Orders</Title>
        <Paragraph>
          This section will provide order monitoring and management.
        </Paragraph>
        <Text type="secondary">No order data connected yet.</Text>
      </>
    ),
  },
];

export default function AdminPage() {
  return (
    <Layout style={{ minHeight: "calc(100vh - 64px)", background: "#f5f5f5" }}>
      <Content style={{ padding: "32px 24px" }}>
        <Card style={{ maxWidth: 1100, margin: "0 auto" }}>
          <Title level={2} style={{ marginTop: 0 }}>
            Admin
          </Title>
          <Tabs defaultActiveKey="players" items={tabItems} />
        </Card>
      </Content>
    </Layout>
  );
}
