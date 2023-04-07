import React, { ErrorInfo } from "react";

export default class ErrorBoundary extends React.Component<
  { children: React.ReactNode; message: string },
  { hasError: boolean }
> {
  constructor(props: { children: React.ReactNode; message: string }) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError(error: Error) {
    return { hasError: true };
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error({ message: this.props.message, error, errorInfo });
  }

  render() {
    if (this.state.hasError) {
      // You can render any custom fallback UI
      return <h1>Something went wrong. {this.props.message}</h1>;
    }
    return this.props.children;
  }
}
