import Image from "next/image";
import { Button, Text, Title, Stack, Group, Container } from "@mantine/core";

export default function Home() {

  return (
    <div className="flex min-h-screen items-center justify-center bg-zinc-50 font-sans dark:bg-black">
      <main className="flex min-h-screen w-full max-w-3xl flex-col items-center justify-between py-32 px-16 bg-white dark:bg-black sm:items-start">
        {/*<Image*/}
        {/*  className="dark:invert"*/}
        {/*  src="/next.svg"*/}
        {/*  alt="Next.js logo"*/}
        {/*  width={100}*/}
        {/*  height={20}*/}
        {/*  priority*/}
        {/*/>*/}
        <Stack gap="lg" align="flex-start" mt="xl">
          <Title order={1} className="max-w-xs tracking-tight text-black dark:text-zinc-50">
            Willkommen!
          </Title>
          <Text size="lg" c="dimmed" className="max-w-md">
            Dies ist meine Next.js playground app, jetzt mit Mantine!
          </Text>
        </Stack>
        <Group mt="xl">
          <Button
            component="a"
            href="https://petunia.roskenet.de"
            target="_blank"
            // rel="noopener noreferrer"
            variant="outline"
            color='black'
            radius="xs"
            size="md"
          >
            Project Petunia
          </Button>
        </Group>
      </main>
    </div>
  );
}
