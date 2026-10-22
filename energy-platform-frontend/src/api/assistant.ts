import request from '@/utils/request'

export const askAssistant = (question: string) => request.post('/assistant/ask', { question })
