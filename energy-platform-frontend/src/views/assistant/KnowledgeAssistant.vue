<template>
  <div class="assistant-page">
    <div class="page-heading">
      <div><h2>运维知识助手</h2><p>基于项目知识库回答告警、工单、预测和安全问题</p></div>
    </div>
    <el-card>
      <div class="quick-questions">
        <span>常用问题</span>
        <el-button v-for="item in quickQuestions" :key="item" size="small" plain @click="selectQuestion(item)">{{ item }}</el-button>
      </div>
      <el-input v-model="question" type="textarea" :rows="4" maxlength="500" show-word-limit
        placeholder="例如：预测偏差告警出现后应该检查什么？" @keyup.ctrl.enter="ask" />
      <div class="actions">
        <el-button :disabled="loading" @click="clear">清空</el-button>
        <el-button type="primary" :loading="loading" @click="ask">提交问题</el-button>
      </div>
    </el-card>
    <el-card v-if="result" class="answer-card">
      <template #header><strong>回答</strong></template>
      <p class="answer">{{ result.answer }}</p>
      <div v-if="result.sources?.length" class="sources"><strong>依据资料</strong><el-tag v-for="source in result.sources" :key="source">{{ source }}</el-tag></div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { askAssistant } from '@/api/assistant'

const question = ref('预测偏差告警出现后应该检查什么？')
const loading = ref(false)
const result = ref<any>(null)
const quickQuestions = [
  '预测偏差告警出现后应该检查什么？',
  '设备异常告警如何生成并处理工单？',
  '功率预测前需要检查哪些数据？'
]

const selectQuestion = (value: string) => {
  question.value = value
  result.value = null
}

const clear = () => {
  question.value = ''
  result.value = null
}

const ask = async () => {
  if (!question.value.trim()) return ElMessage.warning('请输入问题')
  loading.value = true
  try {
    const response: any = await askAssistant(question.value.trim())
    result.value = response.data
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.assistant-page{max-width:980px;margin:0 auto}.page-heading{display:flex;justify-content:space-between;align-items:flex-start;margin-bottom:18px}.page-heading h2{margin:0 0 6px}.page-heading p{margin:0;color:#667085}.quick-questions{display:flex;align-items:center;flex-wrap:wrap;gap:8px;margin-bottom:14px;color:#667085;font-size:14px}.actions{display:flex;justify-content:flex-end;margin-top:14px}.answer-card{margin-top:18px}.answer{white-space:pre-wrap;line-height:1.8}.sources{display:flex;align-items:center;flex-wrap:wrap;gap:8px;margin-top:16px}
</style>
