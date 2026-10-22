<template><v-chart class="chart" :option="option" autoresize /></template>
<script setup lang="ts">
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart, PieChart, GaugeChart, ScatterChart, HeatmapChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent, TitleComponent } from 'echarts/components'
use([CanvasRenderer, LineChart, BarChart, PieChart, GaugeChart, ScatterChart, HeatmapChart, GridComponent, TooltipComponent, LegendComponent, TitleComponent])
defineProps<{option:any}>()
</script>
