import { ThemeRegistry } from '@/styles/themeRegistry'
import '../styles/globals.css'
import type { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Customers Registration Service',
  description: 'Basis - Challenge',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <ThemeRegistry>
        <body id="root">{children}</body>
      </ThemeRegistry>
    </html>
  )
}
