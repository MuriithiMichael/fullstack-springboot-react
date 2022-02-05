import React, { useState, useEffect } from 'react'
import {deleteStudent, getAllStudents} from "./client";
import {
  Table,
  Layout,
  Menu,
  Breadcrumb,
  Avatar,
  Spin,
  Empty,
  Button,
  Badge,
  Tag,
  Popconfirm,
  Radio, Image, Divider
} from 'antd';
import {
  DesktopOutlined,
  PieChartOutlined,
  FileOutlined,
  TeamOutlined,
  UserOutlined,
  LoadingOutlined,
  PlusOutlined
} from '@ant-design/icons';
import StudentDrawerForm from "./Forms/StudentDrawerForm";

import './App.css';
import {errorNotification, successNotification} from "./Notification";

const { Header, Content, Footer, Sider } = Layout;
const { SubMenu } = Menu;

const removeStudent = (studentId, callback) => {
  deleteStudent(studentId).then(() => {
    successNotification("Student deleted", `Student with id ${studentId} deleted`)
    callback();
  }).catch(err => {
      console.log(err.response);
      err.response.json().then(res => {
      console.log(res)
      errorNotification(
          "There was an issue",
          `${res.message} [statusCode: ${err.response.status}] [${err.response.statusText}]`
      )
    })
  })
}

const columns = (fetchStudents) => [
  {
    title: '',
    key: 'avatar',
    render: (text, student) =>(
        <Avatar size='large'>
          {`${student.firstName.charAt(0).toUpperCase()}${student.lastName.charAt(0).toUpperCase()}`}
        </Avatar>
    )
  },
  {
    title: 'Student Id',
    dataIndex: 'studentId',
    key: 'studentId'
  },
  {
    title: 'First Name',
    dataIndex: 'firstName',
    key: 'firstName'
  },
  {
    title: 'Last Name',
    dataIndex: 'lastName',
    key: 'lastName'
  },
  {
    title: 'Email',
    dataIndex: 'email',
    key: 'email'
  },
  {
    title: 'Gender',
    dataIndex: 'gender',
    key: 'gender'
  },
  {
    title: 'Actions',
    key: 'actions',
    render:(text, student) =>
        <Radio.Group>
          <Popconfirm
              placement="topRight"
              title={`Are you sure to delete ${student.firstName} ${student.lastName}`}
              onConfirm={() => removeStudent(student.studentId, fetchStudents)}
              okText="Yes"
              cancelText="No"
          >
            <Radio.Button value="small">Delete</Radio.Button>
          </Popconfirm>
          <Radio.Button value="small">Edit</Radio.Button>
        </Radio.Group>
  }

];

const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />

function App() {
  const [students, setStudents] = useState([]);
  const [collapsed, setCollapsed] = useState(false);
  const [fetching , setFetching] = useState(true);
  const [showDrawer, setShowDrawer] = useState(false);

/*  const fetchStudents = useCallback(() => {
    if (fetching) {
      return <Spin indicator={antIcon}/>
    }
    getAllStudents()
    .then(res => res.json())
        .then(data => {
          console.log(data);
          setStudents(data);
          setFetching(false);
        })
  }, [fetching])*/

  const fetchStudents = () => {
    getAllStudents()
        .then(res => res.json())
        .then(data => {
          setStudents(data);
        }).catch(err => {
          console.log(err.response);
          err.response.json().then(res => {
            console.log(res)
            errorNotification(
                "There was an issue",
                `${res.message} [statusCode: ${err.response.status}] [${err.response.statusText}]`,
                "bottomLeft"
                )
          })
        }).finally(() => setFetching(false))
  }

  useEffect(() => {
    console.log("component is mounted");
    fetchStudents();
  }, []);

  const renderStudents = () => {
    if (fetching) {
      return <Spin indicator={antIcon}/>
    }
    if (students.length <= 0) {
      return <>
          <Button
              onClick={() => setShowDrawer(!showDrawer)}
              type="primary" shape="round" icon={<PlusOutlined/>} size="small">
              Add New Student
          </Button>
          <StudentDrawerForm
              showDrawer={showDrawer}
              setShowDrawer={setShowDrawer}
              fetchStudents={fetchStudents}
          />
          <Empty/>
      </>
    }
    return <>
      <StudentDrawerForm
          showDrawer={showDrawer}
          setShowDrawer={setShowDrawer}
          fetchStudents={fetchStudents}
      />
    <Table
        dataSource={students}
        columns={columns(fetchStudents)}
        bordered
        title={() =>
            <>
              <Tag>Number of students</Tag>
              <Badge count={students.length} className="site-badge-count-4"/>
              <br/><br/>
              <Button
                onClick={() => setShowDrawer(!showDrawer)}
                type="primary" shape="round" icon={<PlusOutlined/>} size="small">
                Add New Student
              </Button>
            </>
        }
        footer={() => 'Footer'}
        pagination={{ pageSize: 50}}
        scroll={{ y: 400 }}
        rowKey={(student) => student.studentId}
    />
    </>
  }

  return <Layout style={{ minHeight: '100vh' }}>
    <Sider collapsible collapsed={collapsed}
           onCollapse={setCollapsed}>
      <div className="logo" />
      <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
        <Menu.Item key="1" icon={<PieChartOutlined />}>
          Option 1
        </Menu.Item>
        <Menu.Item key="2" icon={<DesktopOutlined />}>
          Option 2
        </Menu.Item>
        <SubMenu key="sub1" icon={<UserOutlined />} title="User">
          <Menu.Item key="3">Tom</Menu.Item>
          <Menu.Item key="4">Bill</Menu.Item>
          <Menu.Item key="5">Alex</Menu.Item>
        </SubMenu>
        <SubMenu key="sub2" icon={<TeamOutlined />} title="Team">
          <Menu.Item key="6">Team 1</Menu.Item>
          <Menu.Item key="8">Team 2</Menu.Item>
        </SubMenu>
        <Menu.Item key="9" icon={<FileOutlined />}>
          Files
        </Menu.Item>
      </Menu>
    </Sider>
    <Layout className="site-layout">
      <Header className="site-layout-background" style={{ padding: 0 }} />
      <Content style={{ margin: '0 16px' }}>
        <Breadcrumb style={{ margin: '16px 0' }}>
          <Breadcrumb.Item>User</Breadcrumb.Item>
          <Breadcrumb.Item>Bill</Breadcrumb.Item>
        </Breadcrumb>
        <div className="site-layout-background" style={{ padding: 24, minHeight: 360 }}>
          {renderStudents()}
        </div>
      </Content>
      <Footer style={{ textAlign: 'center' }}>
        <Image
            width={75}
            src="https://user-images.githubusercontent.com/8687350/152641906-f2d5aed7-6f51-401d-9c7f-ab7b9ac434ec.PNG"
        />
        <Divider>
          <a
              rel="noopener noreferrer"
              target="_blank"
              href="https://amigoscode.com/courses/full-stack-spring-boot-react/">
            Tenabo Systems©2021 Created by Tenabo Treq Ltd.
          </a>
        </Divider>
      </Footer>
    </Layout>
  </Layout>
}

export default App;
