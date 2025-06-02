import * as React from 'react'
import {
  Activity,
  Archive,
  Command,
  Files,
  Settings,
  Shield,
  Trash2,
  Users,
} from 'lucide-react'

import { NavUser } from '@/components/nav-user'
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar'

const navigation = [
  {
    title: 'File Management',
    items: [
      { name: 'All Files', icon: Files, id: 'files' },
      { name: 'Shared with Me', icon: Users, id: 'shared' },
      { name: 'Recent', icon: Activity, id: 'recent' },
      { name: 'Archived', icon: Archive, id: 'archived' },
      { name: 'Trash', icon: Trash2, id: 'trash' },
    ],
  },
  {
    title: 'Administration',
    items: [
      { name: 'User Management', icon: Users, id: 'users' },
      { name: 'Activity Logs', icon: Activity, id: 'activity' },
      { name: 'Security', icon: Shield, id: 'security' },
      { name: 'Settings', icon: Settings, id: 'settings' },
    ],
  },
]

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  return (
    <Sidebar variant="inset" {...props}>
      <SidebarHeader>
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton size="lg" asChild>
              <a href="#">
                <div className="bg-sidebar-primary text-sidebar-primary-foreground flex aspect-square size-8 items-center justify-center rounded-lg">
                  <Command className="size-4" />
                </div>
                <div className="grid flex-1 text-left text-sm leading-tight">
                  <span className="truncate font-medium">Acme Inc</span>
                  <span className="truncate text-xs">Enterprise</span>
                </div>
              </a>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarHeader>
      <SidebarContent>
        {navigation.map((section) => (
          <SidebarGroup key={section.title}>
            <SidebarGroupLabel>{section.title}</SidebarGroupLabel>
            <SidebarGroupContent>
              <SidebarMenu>
                {section.items.map((item) => (
                  <SidebarMenuItem key={item.id}>
                    <SidebarMenuButton
                      onClick={() => console.log(`Navigating to ${item.id}`)}
                      isActive={false}
                      className="w-full justify-start"
                    >
                      <item.icon className="h-4 w-4" />
                      {item.name}
                    </SidebarMenuButton>
                  </SidebarMenuItem>
                ))}
              </SidebarMenu>
            </SidebarGroupContent>
          </SidebarGroup>
        ))}
      </SidebarContent>
      <SidebarFooter>
        <NavUser />
      </SidebarFooter>
    </Sidebar>
  )
}
