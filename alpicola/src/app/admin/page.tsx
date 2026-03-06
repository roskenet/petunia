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

const apiBaseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? "";

async function requestJson<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${apiBaseUrl}${path}`, {
    credentials: "include",
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers ?? {}),
    },
  });

  if (response.status === 401 || response.redirected) {
    window.location.href = `${apiBaseUrl}/oauth2/authorization/keycloak`;
    throw new Error("Not authenticated");
  }

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `Request failed with status ${response.status}`);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
}

function PlayersTab() {
  const [form] = Form.useForm<PlayerFormValues>();
  const [players, setPlayers] = useState<PlayerDto[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingPlayer, setEditingPlayer] = useState<PlayerDto | null>(null);
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
          <Space>
            <Button onClick={() => openEditModal(record)}>Edit</Button>
            <Popconfirm
              title={`Delete ${record.player_name}?`}
              description="This cannot be undone."
              okText="Delete"
              cancelText="Cancel"
              onConfirm={() => void handleDelete(record.player_name)}
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
    setEditingPlayer(null);
    form.setFieldsValue({ player_name: "", balance: 0 });
    setIsModalOpen(true);
  };

  const openEditModal = (player: PlayerDto) => {
    setEditingPlayer(player);
    form.setFieldsValue({
      player_name: player.player_name,
      balance: player.balance,
    });
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setEditingPlayer(null);
    form.resetFields();
  };

  const handleSave = async () => {
    const values = await form.validateFields();
    setIsSaving(true);

    try {
      if (editingPlayer) {
        await requestJson<PlayerDto>(`/api/admin/players/${encodeURIComponent(editingPlayer.player_name)}`, {
          method: "PUT",
          body: JSON.stringify(values),
        });
        message.success("Player updated");
      } else {
        const payload: CreatePlayerPayload = {
          player_name: values.player_name,
          initial_balance: values.balance,
        };
        await requestJson<PlayerDto>("/api/admin/players", {
          method: "POST",
          body: JSON.stringify(payload),
        });
        message.success("Player created");
      }

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
          <Text type="secondary">Create, edit and delete player accounts.</Text>
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
        title={editingPlayer ? "Edit Player" : "Create Player"}
        open={isModalOpen}
        confirmLoading={isSaving}
        onOk={() => void handleSave()}
        onCancel={closeModal}
        okText={editingPlayer ? "Save" : "Create"}
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

const tabItems = [
  {
    key: "players",
    label: "Players",
    children: <PlayersTab />,
  },
  {
    key: "securities",
    label: "Securities",
    children: (
      <>
        <Title level={4}>Securities</Title>
        <Paragraph>
          This section will list and manage available securities.
        </Paragraph>
        <Text type="secondary">No securities data connected yet.</Text>
      </>
    ),
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
